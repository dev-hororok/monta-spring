package com.hororok.monta.dto.response.character;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CharacterInfoResponseDto {
    private String status;
    private Data data;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Data {
        private Character character;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Character {
        @JsonProperty("character_id")
        private UUID characterId;
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
    }
}
