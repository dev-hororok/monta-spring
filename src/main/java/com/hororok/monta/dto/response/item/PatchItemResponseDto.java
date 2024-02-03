package com.hororok.monta.dto.response.item;

import com.hororok.monta.entity.Item;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
public class PatchItemResponseDto {

    private String status;
    private Data data;

    public PatchItemResponseDto(Item item) {
        this.status = "success";
        this.data = new Data(new GetItemResponseDto.GetItemDto(item));
    }

    @Getter
    @AllArgsConstructor
    public static class Data {
        private GetItemResponseDto.GetItemDto item;
    }

}
