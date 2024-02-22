package com.hororok.monta.service;

import com.hororok.monta.dto.request.palette.UpdatePaletteRequestDto;
import com.hororok.monta.dto.request.palette.CreatePaletteRequestDto;
import com.hororok.monta.dto.response.DeleteResponseDto;
import com.hororok.monta.dto.response.FailResponseDto;
import com.hororok.monta.dto.response.palette.GetPalettesResponseDto;
import com.hororok.monta.dto.response.palette.UpdatePaletteResponseDto;
import com.hororok.monta.dto.response.palette.CreatePaletteResponseDto;
import com.hororok.monta.entity.Palette;
import com.hororok.monta.repository.PaletteRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class PaletteService {

    public final PaletteRepository paletteRepository;

    @Transactional
    public ResponseEntity<?> getPalettes() {
        List<Palette> palettes = paletteRepository.findAll();
        return ResponseEntity.ok(new GetPalettesResponseDto(palettes));
    }

    @Transactional
    public ResponseEntity<?> postPalettes(CreatePaletteRequestDto requestDto) {
        Palette savePalette = paletteRepository.save(new Palette(requestDto));
        return ResponseEntity.status(HttpStatus.CREATED).body(new CreatePaletteResponseDto(savePalette.getId()));
    }

    @Transactional
    public ResponseEntity<?> patchPalette(int paletteId, UpdatePaletteRequestDto requestDto) {

        Optional<Palette> optionalPalette = paletteRepository.findById(paletteId);
        if(optionalPalette.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new FailResponseDto(HttpStatus.NOT_FOUND.name(), Collections.singletonList("해당 팔레트를 찾을 수 없습니다.")));
        }

        Palette palette = optionalPalette.get();

        String name = requestDto.getName();
        String grade = requestDto.getGrade();
        String lightColor = requestDto.getLightColor();
        String normalColor = requestDto.getNormalColor();
        String darkColor = requestDto.getDarkColor();
        String darkerColor = requestDto.getDarkerColor();

        if(requestDto.getName().isBlank()) name = palette.getName();
        if(requestDto.getGrade().isBlank()) grade = palette.getGrade();
        if(requestDto.getLightColor().isBlank()) lightColor = palette.getLightColor();
        if(requestDto.getNormalColor().isBlank()) normalColor = palette.getNormalColor();
        if(requestDto.getDarkColor().isBlank()) darkColor = palette.getDarkColor();
        if(requestDto.getDarkerColor().isBlank()) darkerColor = palette.getDarkerColor();

        palette.updatePalette(name, grade, lightColor, normalColor, darkColor, darkerColor);
        Palette savePalette = paletteRepository.save(palette);

        return ResponseEntity.ok(new UpdatePaletteResponseDto(savePalette));
    }

    @Transactional
    public ResponseEntity<?> deletePalette(int paletteId) {

        Optional<Palette> optionalPalette = paletteRepository.findById(paletteId);
        if(optionalPalette.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new FailResponseDto(HttpStatus.NOT_FOUND.name(), Collections.singletonList("해당 팔레트를 찾을 수 없습니다.")));
        }

        Palette palette = optionalPalette.get();
        paletteRepository.delete(palette);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(new DeleteResponseDto());
    }

}
