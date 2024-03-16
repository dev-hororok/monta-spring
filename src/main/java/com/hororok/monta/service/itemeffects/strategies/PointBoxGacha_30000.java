package com.hororok.monta.service.itemeffects.strategies;

import com.hororok.monta.dto.response.FailResponseDto;
import com.hororok.monta.dto.response.itemInventory.UsePointBoxGachaResponseDto;
import com.hororok.monta.entity.Item;
import com.hororok.monta.entity.ItemInventory;
import com.hororok.monta.entity.Member;
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
import java.util.Random;

// Point 주머니 뽑기 (동 50%, 금 30%, 다이아 20%)
@EffectCode(30000)
@Component
public class PointBoxGacha_30000 implements EffectCodeStrategy {
    private final ItemInventoryRepository itemInventoryRepository;
    private final ItemRepository itemRepository;

    @Autowired
    public PointBoxGacha_30000(ItemInventoryRepository itemInventoryRepository, ItemRepository itemRepository) {
        this.itemInventoryRepository = itemInventoryRepository;
        this.itemRepository = itemRepository;
    }

    @Override
    public ResponseEntity<?> useItem(ItemInventory itemInventory, Member member) {
        Random random = new Random();
        double randomValue = random.nextDouble();

        // point box 등급 랜덤 추출
        int effectCode;
        if(randomValue < 0.5) {
             effectCode = 30001;
        } else if (randomValue < 0.8) {
            effectCode = 30002;
        } else {
            effectCode = 30003;
        }

        // item (point 주머니) 정보 추출
        Optional<Item> findItem = itemRepository.findOneByEffectCode(effectCode);
        if(findItem.isEmpty()) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new FailResponseDto(HttpStatus.INTERNAL_SERVER_ERROR.name(),
                            Collections.singletonList("point 주머니 아이템이 없습니다. (운영자에게 문의해주세요)")));
        }
        Item item = findItem.get();

        // item Inventory 존재 하지 않으면 새로 저장, 존재 하면 수량 update
        Optional<ItemInventory> findItemInventory = itemInventoryRepository.findByItemIdAndMemberId(item.getId(), member.getId());
        ItemInventory saveItemInventory;
        if(findItemInventory.isEmpty()) {
            saveItemInventory = itemInventoryRepository.save(new ItemInventory(item, member, 1));
        } else {
            ItemInventory existingItemInventory = findItemInventory.get();
            existingItemInventory.updateQuantity(existingItemInventory.getQuantity() + 1);
            saveItemInventory = itemInventoryRepository.save(existingItemInventory);
        }

        // ItemInventory 수량 줄이고 삭제
        itemInventory.updateQuantity(itemInventory.getQuantity() - 1);
        itemInventoryRepository.save(itemInventory);
        itemInventoryRepository.delete(itemInventory);

        return ResponseEntity.status(HttpStatus.OK).body(new UsePointBoxGachaResponseDto(saveItemInventory.getItem()));
    }
}
