package com.hororok.monta.dto.response.item;

import com.hororok.monta.entity.Item;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
public class GetItemsResponseDto {

    private String status;
    private Data data;

    public GetItemsResponseDto(List<Item> items) {
        this.status = "success";
        this.data = new Data(convertToDtoList(items));
    }

    private List<GetItemResponseDto.GetItemDto> convertToDtoList(List<Item> items) {
        List<GetItemResponseDto.GetItemDto> list = new ArrayList<>();
        for(Item item : items) {
            list.add(new GetItemResponseDto.GetItemDto(item));
        }
        return list;
    }

    @Getter
    @AllArgsConstructor
    public static class Data {
        private List<GetItemResponseDto.GetItemDto> items;
    }
}
