package com.hororok.monta.dto.response.character;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.hororok.monta.entity.Character;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class GetCharacterResponseDto {
    private String status;
    private Data data;

    public GetCharacterResponseDto(Character character) {
        this.status = "success";
        this.data = new Data(new GetCharacterDto(character));
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Data {
        private GetCharacterDto getCharacterDto;
    }

    @Getter
    @NoArgsConstructor
    public static class GetCharacterDto {
        @JsonProperty("character_id")
        private int characterId;

        private String name;
        private String description;

        @JsonProperty("image_url")
        private String imageUrl;
        private String grade;

        @JsonProperty("sell_price")
        private int sellPrice;

        @JsonProperty("created_at")
        private LocalDateTime createdAt;

        @JsonProperty("updated_at")
        private LocalDateTime updatedAt;

        public GetCharacterDto(Character character) {
            this.characterId = character.getId();
            this.name = character.getName();
            this.description = character.getDescription();
            this.imageUrl = character.getImageUrl();
            this.grade = character.getGrade();
            this.sellPrice = character.getSellPrice();
            this.createdAt = character.getCreatedAt();
            this.updatedAt = character.getUpdatedAt();
        }
    }
}
