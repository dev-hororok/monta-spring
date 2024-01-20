package com.hororok.monta.dto.request.palette;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.hororok.monta.entity.PaletteGrade;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PatchPaletteRequestDto {

    @NotBlank(message = "이름 필수 입력 값입니다.")
    private String name;

    @NotNull(message = "유효하지 않은 등급이 입력되었습니다.")
    private PaletteGrade grade;

    @NotBlank(message = "light_color 필수 입력 값입니다.")
    @JsonProperty("light_color")
    private String lightColor;

    @NotBlank(message = "normal_color 필수 입력 값입니다.")
    @JsonProperty("normal_color")
    private String normalColor;

    @NotBlank(message = "dark_color 필수 입력 값입니다.")
    @JsonProperty("dark_color")
    private String darkColor;

    @NotBlank(message = "darker_color 필수 입력 값입니다.")
    @JsonProperty("darker_color")
    private String darkerColor;

}
