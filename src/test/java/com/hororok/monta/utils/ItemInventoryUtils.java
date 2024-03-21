package com.hororok.monta.utils;

import com.hororok.monta.repository.ItemInventoryTestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ItemInventoryUtils {
    @Autowired
    private ItemInventoryTestRepository itemInventoryTestRepository;

    public void deleteTestData(long itemInventoryId) {
        itemInventoryTestRepository.deleteTestData(itemInventoryId);
    }
}
