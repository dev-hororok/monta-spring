package com.hororok.monta.dto.response.palette;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
public class CreatePaletteResponseDto {

    private String status;
    private Data data;

    public CreatePaletteResponseDto(int paletteId) {
        this.status = "success";
        this.data = new Data(paletteId);
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Data{
        @JsonProperty("palette_id")
        private int paletteId;
    }
}
