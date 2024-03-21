package com.hororok.monta.dto.response.itemInventory;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.hororok.monta.entity.Item;
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
        this.data = new Data("Consumable Item Acquisition", new UsePointBoxGachaDto(item));
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

        @JsonProperty("item_type")
        private String itemType;

        private String name;
        private String grade;
        private String description;

        @JsonProperty("image_url")
        private String imageUrl;

        private int cost;

        @JsonProperty("required_study_time")
        private Integer requiredStudyTime;

        @JsonProperty("effect_code")
        private int effectCode;

        @JsonProperty("is_hidden")
        private Boolean isHidden;

        public UsePointBoxGachaDto(Item item) {
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
