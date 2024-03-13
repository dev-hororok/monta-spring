package com.hororok.monta.controller;

import com.hororok.monta.dto.request.palette.UpdatePaletteRequestDto;
import com.hororok.monta.dto.request.palette.CreatePaletteRequestDto;
import com.hororok.monta.handler.CustomValidationException;
import com.hororok.monta.service.PaletteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class PaletteController {

    private final PaletteService paletteService;

    @PostMapping("/admin/palettes")
    public ResponseEntity<?> postPaletteDetails(@Valid @RequestBody CreatePaletteRequestDto requestDto, BindingResult bindingResult) {

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

    @GetMapping("/admin/palettes")
    public ResponseEntity<?> getPaletteList() {
        return paletteService.getPalettes();
    }

    @PatchMapping("/admin/palettes/{paletteId}")
    public ResponseEntity<?> patchPaletteDetails(@Valid @RequestBody UpdatePaletteRequestDto requestDto, BindingResult bindingResult, @PathVariable int paletteId) {

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
