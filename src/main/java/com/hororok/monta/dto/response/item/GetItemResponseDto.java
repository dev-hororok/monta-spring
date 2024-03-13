package com.hororok.monta.dto.response.item;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.hororok.monta.entity.Item;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class GetItemResponseDto {
    private String status;
    private Data data;

    public GetItemResponseDto(Item item) {
        this.status = "success";
        this.data = new Data(new GetItemDto(item));
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Data {
        private GetItemDto item;
    }

    @Getter
    @NoArgsConstructor
    public static class GetItemDto {

        @JsonProperty("item_id")
        private long itemId;

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

        @JsonProperty("created_at")
        private LocalDateTime createdAt;

        @JsonProperty("updated_at")
        private LocalDateTime updatedAt;

        public GetItemDto(Item item) {
            itemId = item.getId();
            itemType = item.getItemType();
            name = item.getName();
            grade = item.getGrade();
            description = item.getDescription();
            imageUrl = item.getImageUrl();
            cost = item.getCost();
            requiredStudyTime = item.getRequiredStudyTime();
            effectCode = item.getEffectCode();
            isHidden = item.getIsHidden();
            createdAt = item.getCreatedAt();
            updatedAt = item.getUpdatedAt();
        }
    }
}
