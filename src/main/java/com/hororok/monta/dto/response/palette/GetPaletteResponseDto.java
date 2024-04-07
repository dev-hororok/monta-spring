package com.hororok.monta.dto.response.palette;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.hororok.monta.entity.Palette;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class GetPaletteResponseDto {
    private String status;
    private Data data;

    public GetPaletteResponseDto(Palette palette) {
        this.status = "success";
        this.data = new Data(new GetPaletteDto(palette));
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Data {
        private GetPaletteDto palette;
    }

    @Getter
    @NoArgsConstructor
    public static class GetPaletteDto {
        @JsonProperty("palette_id")
        private int paletteId;

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

        public GetPaletteDto(Palette palette) {
            this.paletteId = palette.getId();
            this.name = palette.getName();
            this.grade = palette.getGrade();
            this.lightColor = palette.getLightColor();
            this.normalColor = palette.getNormalColor();
            this.darkColor = palette.getDarkColor();
            this.darkerColor = palette.getDarkerColor();
        }
    }
}
