package com.hororok.monta.utils;

import com.hororok.monta.dto.request.item.CreateItemRequestDto;
import com.hororok.monta.entity.Item;
import com.hororok.monta.repository.ItemTestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ItemUtils {
    @Autowired
    private ItemTestRepository itemTestRepository;

    public CreateItemRequestDto createItemRequestDto(boolean isRight) {
        if(isRight) {
            return new CreateItemRequestDto("Test Item " + Math.ceil(Math.random()*100), "테스트 아이템", "B", "TestItemDes", "TestItemUrl", 200, 3600, 10000, false);
        } else {
            return new CreateItemRequestDto("", "", "", "", "", 0, 0, 0, false);
        }
    }

    public CreateItemRequestDto createItemRequestDtoByItemType(String itemType) {
        return new CreateItemRequestDto(itemType, "테스트 아이템", "B", "TestItemDes", "TestItemUrl", 200, 3600, 10000, false);
    }

    public void deleteTestData(int itemId) {
        itemTestRepository.deleteTestData(itemId);
    }

    public Item saveItem(CreateItemRequestDto requestDto) {
        return itemTestRepository.save(new Item(requestDto));
    }
}
