package com.hororok.monta.dto.response.character;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.springframework.http.HttpStatus;
import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CharacterResponseDto {
    private int status;
    private Data data;

    public void setCharacterId(UUID characterId) {
        if (this.data == null) {
            this.data = new Data();
        }
        this.data.setCharacterId(characterId);
        this.status = HttpStatus.CREATED.value();
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Data {
        @JsonProperty("character_id")
        private UUID characterId;
    }
}
