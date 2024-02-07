package com.hororok.monta.dto.response.character;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CharacterResponseDto {
    private String status;
    private Data data;

    public void setCharacterId(int characterId) {
        if (this.data == null) {
            this.data = new Data();
        }
        this.data.setCharacterId(characterId);
        this.status = "success";
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Data {
        @JsonProperty("character_id")
        private int characterId;
    }
}
