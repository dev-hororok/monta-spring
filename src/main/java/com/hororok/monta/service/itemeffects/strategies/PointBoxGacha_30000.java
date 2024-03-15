package com.hororok.monta.service.itemeffects.strategies;

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

import java.util.Optional;
import java.util.Random;

// PointBox 뽑기 (B 50%, A 30%, S 20%)
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

        Optional<Item> findItem = itemRepository.findOneByEffectCode(effectCode);
        Item item = findItem.get();

        Optional<ItemInventory> findItemInventory = itemInventoryRepository.findByItemIdAndMemberId(item.getId(), member.getId());
        ItemInventory saveItemInventory;
        if(findItemInventory.isPresent()) {
            ItemInventory existingItemInventory = findItemInventory.get();
            existingItemInventory.updateQuantity(existingItemInventory.getQuantity() + 1);
            saveItemInventory = itemInventoryRepository.save(existingItemInventory);
        } else {
            saveItemInventory = itemInventoryRepository.save(new ItemInventory(findItem.get(), member, 1));
        }

        // ItemInventory 수량 줄이고 삭제
        itemInventory.updateQuantity(itemInventory.getQuantity()-1);
        itemInventoryRepository.save(itemInventory);
        itemInventoryRepository.delete(itemInventory);

        return ResponseEntity.status(HttpStatus.OK).body(new UsePointBoxGachaResponseDto(saveItemInventory));
    }
}
