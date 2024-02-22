package com.hororok.monta.dto.response.palette;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.hororok.monta.entity.Palette;
import lombok.AllArgsConstructor;
import lombok.Getter;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor
public class GetPalettesResponseDto {

    private String status;
    private Data data;

    public GetPalettesResponseDto(List<Palette> palettes) {
        this.status = "success";
        this.data = new Data(convertToDtoList(palettes));
    }

    private List<GetPalettesDto> convertToDtoList(List<Palette> palettes) {
        List<GetPalettesDto> list = new ArrayList<>();
        for(Palette palette : palettes) {
            list.add(new GetPalettesDto(palette));
        }
        return list;
    }

    @Getter
    @AllArgsConstructor
    public static class Data {
        private List<GetPalettesDto> palettes;
    }

    @Getter
    @AllArgsConstructor
    public static class GetPalettesDto {

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

        @JsonProperty("created_at")
        private LocalDateTime createdAt;

        @JsonProperty("updated_at")
        private LocalDateTime updatedAt;

        public GetPalettesDto(Palette palette) {
            this.paletteId = palette.getId();
            this.name = palette.getName();
            this.grade = palette.getGrade();
            this.lightColor = palette.getLightColor();
            this.normalColor = palette.getNormalColor();
            this.darkColor = palette.getDarkColor();
            this.darkerColor = palette.getDarkerColor();
            this.createdAt = palette.getCreatedAt();
            this.updatedAt = palette.getUpdatedAt();
        }
    }
}
