package com.hororok.monta.dto.response.character;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CreateCharacterResponseDto {
    private String status;
    private Data data;

    public CreateCharacterResponseDto(int characterId) {
        this.status = "success";
        this.data = new Data(characterId);
    }

    @Getter
    @AllArgsConstructor
    public static class Data {
        @JsonProperty("character_id")
        private int characterId;
    }
}
