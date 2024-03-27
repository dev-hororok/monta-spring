package com.hororok.monta.utils;

import com.hororok.monta.dto.request.palette.CreatePaletteRequestDto;
import com.hororok.monta.entity.Palette;
import com.hororok.monta.repository.PaletteTestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PaletteUtils {
    @Autowired
    private PaletteTestRepository paletteTestRepository;

    public CreatePaletteRequestDto createPaletteRequestDto(boolean isRight) {
        if(isRight) {
            return new CreatePaletteRequestDto("Test Palette " + Math.ceil(Math.random()*100), "B", "#000000", "#000000", "#000000", "#000000");
        } else {
            return new CreatePaletteRequestDto("", "", "", "", "", "");
        }
    }

    public void deleteTestData(int paletteId) {
        paletteTestRepository.deleteTestData(paletteId);
    }

    public Palette savePalette(CreatePaletteRequestDto requestDto) {
        return paletteTestRepository.save(new Palette(requestDto));
    }
}
