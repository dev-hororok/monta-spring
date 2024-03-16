package com.hororok.monta.service.itemeffects.strategies;

import com.hororok.monta.dto.response.FailResponseDto;
import com.hororok.monta.dto.response.itemInventory.UsePaletteTicketGachaResponseDto;
import com.hororok.monta.entity.*;
import com.hororok.monta.repository.ItemInventoryRepository;
import com.hororok.monta.repository.ItemRepository;
import com.hororok.monta.service.itemeffects.EffectCode;
import com.hororok.monta.service.itemeffects.EffectCodeStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Optional;

// Palette 뽑기권 뽑기 (1~20장)
@EffectCode(20000)
@Component
public class PaletteGacha_20000 implements EffectCodeStrategy {
    private final ItemRepository itemRepository;
    private final ItemInventoryRepository itemInventoryRepository;

    @Autowired
    public PaletteGacha_20000(ItemRepository itemRepository, ItemInventoryRepository itemInventoryRepository) {
        this.itemRepository = itemRepository;
        this.itemInventoryRepository = itemInventoryRepository;
    }

    @Override
    public ResponseEntity<?> useItem(ItemInventory itemInventory, Member member) {
        // 1~20 중에 랜덤 수 생성
        int randomValue = 1 + (int)(Math.random() * (20));

        // '팔레트 뽑기 (20001)' 아이템 정보 가져 오기
        Optional<Item> findItem = itemRepository.findOneByEffectCode(20001);
        if(findItem.isEmpty()) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new FailResponseDto(HttpStatus.INTERNAL_SERVER_ERROR.name()
                            , Collections.singletonList("팔레트 뽑기 아이템이 없습니다. (운영자에게 문의해주세요)")));
        }
        Item item = findItem.get();

        // ItemInventory 수량 update (존재하지 않으면 새로 만들어주고, 존재하면 수량 update)
        Optional<ItemInventory> findItemInventory = itemInventoryRepository.findByItemIdAndMemberId(item.getId(), member.getId());
        ItemInventory saveItemInventory;
        if(findItemInventory.isEmpty()) {
            // Item Inventory 새로 저장
            saveItemInventory = itemInventoryRepository.save(new ItemInventory(item, member, randomValue));
        }
        else {
            // Item Inventory 수량 update
            ItemInventory itemInventoryOfPaletteGacha = findItemInventory.get();
            itemInventoryOfPaletteGacha.updateQuantity(itemInventoryOfPaletteGacha.getQuantity() + randomValue);
            saveItemInventory = itemInventoryRepository.save(itemInventoryOfPaletteGacha);
        }

        // 사용한 아이템 수량 줄이기
        itemInventory.updateQuantity(itemInventory.getQuantity() - 1);
        itemInventoryRepository.save(itemInventory);

        return ResponseEntity.status(HttpStatus.OK)
                .body(new UsePaletteTicketGachaResponseDto(saveItemInventory.getItem(), randomValue));
    }
}
