package com.hororok.monta.dto.response.itemInventory;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.hororok.monta.entity.Item;
import com.hororok.monta.entity.ItemInventory;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UsePaletteTicketGachaResponseDto {
    private String status;
    private Data data;

    public UsePaletteTicketGachaResponseDto(Item item, int quantity) {
        this.status = "success";
        this.data = new Data("Consumable Item Acquisition", item, quantity);
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Data {
        private String result;
        private Item item;
        private int count;
    }
}
