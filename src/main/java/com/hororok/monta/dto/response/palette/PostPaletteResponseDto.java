package com.hororok.monta.dto.response.palette;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.hororok.monta.dto.response.PostBaseResponseDto;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
public class PostPaletteResponseDto extends PostBaseResponseDto<PostPaletteResponseDto.Data> {

    @AllArgsConstructor
    public static class Data {
        @JsonProperty("palette_id")
        private Long paletteId;
    }

    public PostPaletteResponseDto(Long paletteId) {
        super(new Data(paletteId));
    }
}
