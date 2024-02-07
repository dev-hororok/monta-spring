package com.hororok.monta.dto.response.palette;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
public class PostPaletteResponseDto {

    private String status;
    private Data data;

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Data{
        @JsonProperty("palette_id")
        private int paletteId;
    }

    public PostPaletteResponseDto(int paletteId) {
        this.data = new Data(paletteId);
        this.status = "success";
    }
}
