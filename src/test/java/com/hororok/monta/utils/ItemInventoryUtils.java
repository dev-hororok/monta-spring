package com.hororok.monta.utils;

import com.hororok.monta.entity.Item;
import com.hororok.monta.entity.ItemInventory;
import com.hororok.monta.entity.Member;
import com.hororok.monta.repository.ItemInventoryTestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ItemInventoryUtils {
    @Autowired
    private ItemInventoryTestRepository itemInventoryTestRepository;

    public ItemInventory saveItemInventory(Item item, Member member, int progress) {
        return itemInventoryTestRepository.save(new ItemInventory(9999L, item, member, item.getItemType(), progress, 1));
    }

    public ItemInventory saveItemInventoryByLeftTime(Item item, Member member) {
        return itemInventoryTestRepository.save(new ItemInventory(9999L, item, member, "Food", 1000, 1));
    }

    public void deleteTestData(long itemInventoryId) {
        itemInventoryTestRepository.deleteTestData(itemInventoryId);
    }

    public ItemInventory findItemInventory(Item item, Member member) {
        return itemInventoryTestRepository.findByMemberIdAndItemId(member.getId(), item.getId());
    }
}
