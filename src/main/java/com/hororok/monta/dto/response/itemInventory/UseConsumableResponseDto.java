package com.hororok.monta.dto.response.itemInventory;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.hororok.monta.entity.Palette;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UseConsumableResponseDto {
    private String status;
    private Data data;

    public UseConsumableResponseDto(Palette palette) {
        this.status = "success";
        this.data = new Data(new RandomPaletteByItemDto(palette));
    }

    @Getter
    @AllArgsConstructor
    public static class Data {
        private RandomPaletteByItemDto palette;
    }

    @Getter
    @NoArgsConstructor
    public static class RandomPaletteByItemDto {

        @JsonProperty("palette_id")
        private long paletteId;

        private String grade;
        private String name;

        @JsonProperty("light_color")
        private String lightColor;

        @JsonProperty("normal_color")
        private String normalColor;

        @JsonProperty("dark_color")
        private String darkColor;

        @JsonProperty("darker_color")
        private String darkerColor;

        public RandomPaletteByItemDto(Palette palette) {
            this.paletteId = palette.getId();
            this.grade = palette.getGrade();
            this.name = palette.getName();
            this.lightColor = palette.getLightColor();
            this.normalColor = palette.getNormalColor();
            this.darkColor = palette.getDarkColor();
            this.darkerColor = palette.getDarkerColor();
        }
    }
}
