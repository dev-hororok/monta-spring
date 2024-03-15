package com.hororok.monta.dto.response.itemInventory;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.hororok.monta.entity.ItemInventory;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UsePointBoxGachaResponseDto {
    private String status;
    private Data data;
    public UsePointBoxGachaResponseDto(ItemInventory itemInventory) {
        this.status = "success";
        this.data = new Data("Point Box Acquisition", new UsePointBoxGachaDto(itemInventory));
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Data {
        private String result;
        private UsePointBoxGachaDto item;
    }

    @Getter
    @NoArgsConstructor
    public static class UsePointBoxGachaDto {
        @JsonProperty("item_id")
        private int itemId;

        @JsonProperty("item_name")
        private String itemName;

        private String description;

        public UsePointBoxGachaDto(ItemInventory itemInventory) {
            this.itemId = itemInventory.getItem().getId();
            this.itemName = itemInventory.getItem().getName();
            this.description = itemInventory.getItem().getDescription();
        }
    }
}
