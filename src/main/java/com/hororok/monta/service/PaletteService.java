package com.hororok.monta.service;

import com.hororok.monta.dto.response.palette.GetPalettesResponseDto;
import com.hororok.monta.entity.Palette;
import com.hororok.monta.repository.PaletteRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PaletteService {

    public final PaletteRepository paletteRepository;

    public PaletteService(PaletteRepository paletteRepository) {
        this.paletteRepository = paletteRepository;
    }

    public ResponseEntity<?> getPalettes() {
        List<Palette> palettes = paletteRepository.findAll();
        return ResponseEntity.ok(new GetPalettesResponseDto(palettes));
    }
}
