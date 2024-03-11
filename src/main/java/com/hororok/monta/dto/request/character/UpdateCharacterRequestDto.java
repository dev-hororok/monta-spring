package com.hororok.monta.dto.request.character;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UpdateCharacterRequestDto {
    private String name;
    private String description;

    @JsonProperty("image_url")
    private String imageUrl;

    private String grade;

    @JsonProperty("sell_price")
    private Integer sellPrice;
}
