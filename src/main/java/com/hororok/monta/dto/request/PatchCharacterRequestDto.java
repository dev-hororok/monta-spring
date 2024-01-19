package com.hororok.monta.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PatchCharacterRequestDto {
    private String name;

    private String description;

    @JsonProperty("image_url")
    private String imageUrl;

    private String grade;

    @JsonProperty("sell_price")
    private Integer sellPrice;
}
