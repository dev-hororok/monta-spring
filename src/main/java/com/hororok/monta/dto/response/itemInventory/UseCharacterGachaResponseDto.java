package com.hororok.monta.dto.response.itemInventory;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.hororok.monta.entity.Character;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UseCharacterGachaResponseDto {
    private String status;
    private Data data;

    public UseCharacterGachaResponseDto(long characterInventoryId, Character character) {
        this.status = "success";
        this.data = new Data(characterInventoryId, "Character Acquisition", new RandomCharacterByItemDto(character));
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Data {
        @JsonProperty("character_inventory_id")
        private long characterInventoryId;

        private String result;
        private RandomCharacterByItemDto character;
    }

    @Getter
    @NoArgsConstructor
    public static class RandomCharacterByItemDto {

        @JsonProperty("character_id")
        private int characterId;

        private String name;
        private String description;
        private String grade;

        @JsonProperty("image_url")
        private String imageUrl;

        @JsonProperty("sell_price")
        private int sellPrice;

        public RandomCharacterByItemDto(Character character) {
            this.characterId = character.getId();
            this.name = character.getName();
            this.description = character.getDescription();
            this.grade = character.getGrade();
            this.imageUrl = character.getImageUrl();
            this.sellPrice = character.getSellPrice();
        }
    }
}
