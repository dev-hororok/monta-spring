package com.hororok.monta.controller;

import com.hororok.monta.dto.request.palette.PatchPaletteRequestDto;
import com.hororok.monta.dto.request.palette.PostPaletteRequestDto;
import com.hororok.monta.handler.CustomValidationException;
import com.hororok.monta.service.PaletteService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@AllArgsConstructor
public class PaletteController {

    private final PaletteService paletteService;

    @GetMapping("/admin/palettes")
    public ResponseEntity<?> getPalettes() {
        return paletteService.getPalettes();
    }

    @PostMapping("/admin/palettes")
    public ResponseEntity<?> postPalettes(@Valid @RequestBody PostPaletteRequestDto requestDto, BindingResult bindingResult) {

        if(bindingResult.hasErrors()) {
            List<String> errors = new ArrayList<>();
            for(FieldError error : bindingResult.getFieldErrors()) {
                errors.add(error.getField() + " : " + error.getDefaultMessage());
            }
            throw new CustomValidationException(errors);
        } else {
            return paletteService.postPalettes(requestDto);
        }
    }

    @PatchMapping("/admin/palettes/{paletteId}")
    public ResponseEntity<?> patchPalette(@Valid @RequestBody PatchPaletteRequestDto requestDto, BindingResult bindingResult, @PathVariable int paletteId) {

        if(bindingResult.hasErrors()) {
            List<String> errors = new ArrayList<>();
            for(FieldError error : bindingResult.getFieldErrors()) {
                errors.add(error.getField() + " : " + error.getDefaultMessage());
            }
            throw new CustomValidationException(errors);
        } else {
            return paletteService.patchPalette(paletteId, requestDto);
        }
    }

    @DeleteMapping("/admin/palettes/{paletteId}")
    public ResponseEntity<?> deletePalette(@PathVariable int paletteId) {
        return paletteService.deletePalette(paletteId);
    }
}
