package com.hororok.monta.dto.request.palette;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UpdatePaletteRequestDto {

    private String name;
    private String grade;

    @JsonProperty("light_color")
    private String lightColor;

    @JsonProperty("normal_color")
    private String normalColor;

    @JsonProperty("dark_color")
    private String darkColor;

    @JsonProperty("darker_color")
    private String darkerColor;
}
