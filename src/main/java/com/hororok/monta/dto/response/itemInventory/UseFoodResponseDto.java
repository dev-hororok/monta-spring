package com.hororok.monta.dto.response.itemInventory;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.hororok.monta.entity.Character;
import com.hororok.monta.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@NoArgsConstructor
public class UseFoodResponseDto {

    private String status;
    private Data data;

    public UseFoodResponseDto(long characterInventoryId, Character character) {
        this.status = "success";
        this.data = new Data(characterInventoryId, new UseFoodDto(character));
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Data {
        private long character_inventory_id;
        private UseFoodDto character;
    }

    @Getter
    @NoArgsConstructor
    public static class UseFoodDto {

        @JsonProperty("character_id")
        private UUID characterId;

        private String name;
        private String description;
        private String grade;

        @JsonProperty("image_url")
        private String imageUrl;

        @JsonProperty("sell_price")
        private int sellPrice;

        public UseFoodDto(Character character) {
            this.characterId = character.getId();
            this.name = character.getName();
            this.description = character.getDescription();
            this.grade = character.getGrade();
            this.imageUrl = character.getImageUrl();
            this.sellPrice = character.getSellPrice();
        }
    }

}
