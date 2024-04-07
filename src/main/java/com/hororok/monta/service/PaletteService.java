package com.hororok.monta.service;

import com.hororok.monta.dto.request.palette.UpdatePaletteRequestDto;
import com.hororok.monta.dto.request.palette.CreatePaletteRequestDto;
import com.hororok.monta.dto.response.DeleteResponseDto;
import com.hororok.monta.dto.response.FailResponseDto;
import com.hororok.monta.dto.response.palette.GetPaletteResponseDto;
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class PaletteService {
    public final PaletteRepository paletteRepository;

    @Transactional
    public ResponseEntity<?> addPaletteDetails(CreatePaletteRequestDto requestDto) {
        Palette savePalette = paletteRepository.save(new Palette(requestDto));
        return ResponseEntity.status(HttpStatus.CREATED).body(new CreatePaletteResponseDto(savePalette.getId()));
    }

    @Transactional
    public ResponseEntity<?> findPalettesList() {
        List<Palette> palettes = paletteRepository.findAll();
        return ResponseEntity.ok(new GetPalettesResponseDto(palettes));
    }

    @Transactional
    public ResponseEntity<?> findPaletteDetails(int paletteId) {
        Optional<Palette> findPalette = paletteRepository.findOneById(paletteId);

        if(findPalette.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new FailResponseDto(HttpStatus.NOT_FOUND.name(), Collections.singletonList("존재하지 않는 팔레트입니다")));
        }
        Palette palette = findPalette.get();
        return ResponseEntity.status(HttpStatus.OK).body(new GetPaletteResponseDto(palette));
    }

    @Transactional
    public ResponseEntity<?> updatePaletteDetails(int paletteId, UpdatePaletteRequestDto requestDto) {

        Optional<Palette> optionalPalette = paletteRepository.findById(paletteId);
        if(optionalPalette.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new FailResponseDto(HttpStatus.NOT_FOUND.name(), Collections.singletonList("해당 팔레트를 찾을 수 없습니다.")));
        }
        Palette palette = optionalPalette.get();

        List<UpdatePaletteRequestDto> list = new ArrayList<>();
        list.add(requestDto);

        String name = palette.getName();
        String grade = palette.getGrade();
        String lightColor = palette.getLightColor();
        String normalColor = palette.getNormalColor();
        String darkColor = palette.getDarkColor();
        String darkerColor = palette.getDarkerColor();

        for (UpdatePaletteRequestDto paletteRequestDto : list) {
            if (paletteRequestDto.getName() != null) name = paletteRequestDto.getName();
            if (paletteRequestDto.getGrade() != null) grade = paletteRequestDto.getGrade();
            if (paletteRequestDto.getLightColor() != null) lightColor = paletteRequestDto.getLightColor();
            if (paletteRequestDto.getNormalColor() != null) normalColor = paletteRequestDto.getNormalColor();
            if (paletteRequestDto.getDarkColor() != null) darkColor = paletteRequestDto.getDarkColor();
            if (paletteRequestDto.getDarkerColor() != null) darkerColor = paletteRequestDto.getDarkerColor();
        }

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
