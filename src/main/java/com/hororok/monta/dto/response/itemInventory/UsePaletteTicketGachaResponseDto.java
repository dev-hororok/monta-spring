package com.hororok.monta.dto.response.itemInventory;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.hororok.monta.entity.ItemInventory;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UsePaletteTicketGachaResponseDto {
    private String status;
    private Data data;
    public UsePaletteTicketGachaResponseDto(ItemInventory itemInventory, int quantity) {
        this.status = "success";
        this.data = new Data("Palette Ticket Acquisition", new UsePaletteTicketGachaDto(itemInventory, quantity));
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Data {
        private String result;
        private UsePaletteTicketGachaDto item;
    }

    @Getter
    @NoArgsConstructor
    public static class UsePaletteTicketGachaDto {
        @JsonProperty("item_id")
        private int itemId;

        @JsonProperty("item_name")
        private String itemName;

        private String description;

        @JsonProperty("earned_quantity")
        private int earnedQuantity;

        @JsonProperty("total_quantity")
        private int totalQuantity;

        public UsePaletteTicketGachaDto(ItemInventory itemInventory, int quantity) {
            this.itemId = itemInventory.getItem().getId();
            this.itemName = itemInventory.getItem().getName();
            this.description = itemInventory.getItem().getDescription();
            this.earnedQuantity = quantity;
            this.totalQuantity = itemInventory.getQuantity();
        }
    }
}
