package com.hororok.monta.dto.request.palette;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PostPaletteRequestDto {

    @NotBlank(message = "필수 입력 값 입니다.")
    private String name;

    @NotBlank(message = "필수 입력 값 입니다.")
    private String grade;

    @NotBlank(message = "필수 입력 값 입니다.")
    @JsonProperty("light_color")
    private String lightColor;

    @NotBlank(message = "필수 입력 값 입니다.")
    @JsonProperty("normal_color")
    private String normalColor;

    @NotBlank(message = "필수 입력 값 입니다.")
    @JsonProperty("dark_color")
    private String darkColor;

    @NotBlank(message = "필수 입력 값 입니다.")
    @JsonProperty("darker_color")
    private String darkerColor;

}