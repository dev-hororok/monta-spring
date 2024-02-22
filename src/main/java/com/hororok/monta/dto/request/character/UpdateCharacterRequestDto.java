package com.hororok.monta.dto.request.character;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class UpdateCharacterRequestDto {
    private String name;
    private String description;

    @JsonProperty("image_url")
    private String imageUrl;

    private String grade;

    @JsonProperty("sell_price")
    private Integer sellPrice;
}
