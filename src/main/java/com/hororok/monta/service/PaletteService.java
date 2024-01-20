package com.hororok.monta.service;

import com.hororok.monta.dto.request.palette.PatchPaletteRequestDto;
import com.hororok.monta.dto.request.palette.PostPaletteRequestDto;
import com.hororok.monta.dto.response.FailResponseDto;
import com.hororok.monta.dto.response.palette.GetPalettesResponseDto;
import com.hororok.monta.dto.response.palette.PatchPaletteResponseDto;
import com.hororok.monta.dto.response.palette.PostPaletteResponseDto;
import com.hororok.monta.entity.Palette;
import com.hororok.monta.entity.PaletteGrade;
import com.hororok.monta.repository.PaletteRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

    @Transactional
    public ResponseEntity<?> patchPalette(Long paletteId, PatchPaletteRequestDto requestDto) {

        Optional<Palette> optionalPalette = paletteRepository.findById(paletteId);
        if(optionalPalette.isEmpty()) {
            List<String> errors = new ArrayList<>();
            errors.add("해당 팔레트를 찾을 수 없습니다.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new FailResponseDto(HttpStatus.NOT_FOUND.value(), "찾을 수 없음", errors));
        }

        Palette palette = optionalPalette.get();

        String name = requestDto.getName();
        PaletteGrade grade = requestDto.getGrade();
        String lightColor = requestDto.getLightColor();
        String normalColor = requestDto.getNormalColor();
        String darkColor = requestDto.getDarkColor();
        String darkerColor = requestDto.getDarkerColor();

        if(requestDto.getName()==null) name = palette.getName();
        if(requestDto.getGrade()==null) grade = palette.getGrade();
        if(requestDto.getLightColor()==null) lightColor = palette.getLightColor();
        if(requestDto.getNormalColor()==null) normalColor = palette.getNormalColor();
        if(requestDto.getDarkColor()==null) darkColor = palette.getDarkColor();
        if(requestDto.getDarkerColor()==null) darkerColor = palette.getDarkerColor();

        palette.updatePalette(name, grade, lightColor, normalColor, darkColor, darkerColor);
        Palette savePalette = paletteRepository.save(palette);

        return ResponseEntity.ok(new PatchPaletteResponseDto(savePalette));
    }
}
