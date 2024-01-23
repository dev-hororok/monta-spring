package com.hororok.monta.dto.response.member;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class GetEggToCharacterResponseDto {
    private int status;
    private Data data;

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Data {
        private Character character;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Character {
        private String characterId;
        private String name;
        private String description;
        private String imageUrl;
        private String grade;
        private int sellPrice;
    }
}