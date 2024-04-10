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
        this.data = new Data("Consumable Item Acquisition", new ItemDto(item), quantity);
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Data {
        private String result;
        private ItemDto item;
        private int count;
    }

    @Getter
    @NoArgsConstructor
    public static class ItemDto {
        @JsonProperty("item_id")
        private int itemId;

        @JsonProperty("item_type")
        private String itemType;

        private String name;
        private String grade;
        private String description;

        @JsonProperty("image_url")
        private String imageUrl;

        private int cost;

        @JsonProperty("required_study_time")
        private int requiredStudyTime;

        @JsonProperty("effect_code")
        private int effectCode;

        @JsonProperty("is_hidden")
        private Boolean isHidden;

        public ItemDto(Item item) {
            this.itemId = item.getId();
            this.itemType = item.getItemType();
            this.name = item.getName();
            this.grade = item.getGrade();
            this.description = item.getDescription();
            this.imageUrl = item.getImageUrl();
            this.cost = item.getCost();
            this.requiredStudyTime = item.getRequiredStudyTime();
            this.effectCode = item.getEffectCode();
            this.isHidden = item.getIsHidden();
        }
    }
}
