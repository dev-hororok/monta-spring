package com.hororok.monta.dto.request.palette;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class CreatePaletteRequestDto {
    @NotBlank(message = "팔레트 이름은 필수 입력 값 입니다.")
    private String name;

    @NotBlank(message = "팔레트 등급은 필수 입력 값 입니다.")
    private String grade;

    @NotBlank(message = "light color 필수 입력 값 입니다.")
    @JsonProperty("light_color")
    private String lightColor;

    @NotBlank(message = "normal color 필수 입력 값 입니다.")
    @JsonProperty("normal_color")
    private String normalColor;

    @NotBlank(message = "dark color 필수 입력 값 입니다.")
    @JsonProperty("dark_color")
    private String darkColor;

    @NotBlank(message = "darker color 필수 입력 값 입니다.")
    @JsonProperty("darker_color")
    private String darkerColor;
}
