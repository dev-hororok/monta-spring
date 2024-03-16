package com.hororok.monta.dto.response.itemInventory;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.hororok.monta.entity.Item;
import com.hororok.monta.entity.ItemInventory;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UsePointBoxGachaResponseDto {
    private String status;
    private Data data;
    public UsePointBoxGachaResponseDto(Item item) {
        this.status = "success";
        this.data = new Data("Consumable Item Acquisition", item);
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Data {
        private String result;
        private Item item;
    }
}
