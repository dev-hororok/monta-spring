package com.hororok.monta.dto.response.character;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.hororok.monta.entity.Character;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor
public class GetCharacterByGradeResponseDto {
    private String status;
    private Data data;

    public GetCharacterByGradeResponseDto(List<Character> characters) {
        this.status = "success";
        this.data = new Data(convertToDtoList(characters));
    }

    private List<GetCharacterByGradeDto> convertToDtoList(List<Character> characters) {
        List<GetCharacterByGradeDto> list = new ArrayList<>();
        for(Character character : characters) {
            list.add(new GetCharacterByGradeDto(character));
        }
        return list;
    }

    @Getter
    @AllArgsConstructor
    public static class Data {
        private List<GetCharacterByGradeDto> characters;
    }

    @Getter
    @NoArgsConstructor
    public static class GetCharacterByGradeDto {
        @JsonProperty("character_id")
        private int characterId;

        private String name;
        private String description;

        @JsonProperty("image_url")
        private String imageUrl;

        private String grade;

        @JsonProperty("sell_price")
        private int sellPrice;

        public GetCharacterByGradeDto(Character character) {
            this.characterId = character.getId();
            this.name = character.getName();
            this.description = character.getDescription();
            this.imageUrl = character.getImageUrl();
            this.grade = character.getGrade();
            this.sellPrice = character.getSellPrice();
        }
    }
}

