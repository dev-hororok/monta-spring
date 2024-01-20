package com.hororok.monta.service;

import com.hororok.monta.dto.request.palette.PostPaletteRequestDto;
import com.hororok.monta.dto.response.palette.GetPalettesResponseDto;
import com.hororok.monta.dto.response.palette.PostPaletteResponseDto;
import com.hororok.monta.entity.Palette;
import com.hororok.monta.repository.PaletteRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PaletteService {

    public final PaletteRepository paletteRepository;

    public PaletteService(PaletteRepository paletteRepository) {
        this.paletteRepository = paletteRepository;
    }

    @Transactional
    public ResponseEntity<?> getPalettes() {
        List<Palette> palettes = paletteRepository.findAll();
        return ResponseEntity.ok(new GetPalettesResponseDto(palettes));
    }

    @Transactional
    public ResponseEntity<?> postPalettes(PostPaletteRequestDto requestDto) {
        Palette savePalette = paletteRepository.save(new Palette(requestDto));
        return ResponseEntity.status(HttpStatus.CREATED).body(new PostPaletteResponseDto(savePalette.getId()));
    }
}
