package com.hororok.monta.dto.response.palette;

import com.hororok.monta.entity.Palette;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.util.List;

@Getter
@AllArgsConstructor
public class GetPalettesResponseDto {

    private int status;
    private Data data;

    @Getter
    @AllArgsConstructor
    public static class Data {
        private List<Palette> palettes;
    }

    public GetPalettesResponseDto(List<Palette> palettes) {
        this.status = HttpStatus.OK.value();
        this.data = new Data(palettes);
    }
}
