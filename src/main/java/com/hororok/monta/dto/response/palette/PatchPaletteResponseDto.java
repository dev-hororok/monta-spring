package com.hororok.monta.dto.response.palette;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.hororok.monta.entity.Palette;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class PatchPaletteResponseDto {

    private String status;
    private Data data;

    public PatchPaletteResponseDto(Palette palette) {
        this.status = "success";
        this.data = new Data(new PatchPaletteDto(palette));
    }

    @Getter
    @AllArgsConstructor
    public static class Data {
        private PatchPaletteDto palette;
    }

    @Getter
    @AllArgsConstructor
    public static class PatchPaletteDto {

        @JsonProperty("palette_id")
        private Long paletteId;

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

        @JsonProperty("created_at")
        private LocalDateTime createdAt;

        @JsonProperty("updated_at")
        private LocalDateTime updatedAt;

        public PatchPaletteDto(Palette palette) {
            paletteId = palette.getId();
            name = palette.getName();
            grade = palette.getGrade();
            lightColor = palette.getLightColor();
            normalColor = palette.getNormalColor();
            darkColor = palette.getDarkColor();
            darkerColor = palette.getDarkerColor();
            createdAt = palette.getCreatedAt();
            updatedAt = LocalDateTime.now();
        }
    }
}
