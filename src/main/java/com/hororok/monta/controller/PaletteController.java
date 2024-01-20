package com.hororok.monta.controller;

import com.hororok.monta.dto.request.palette.PostPaletteRequestDto;
import com.hororok.monta.handler.CustomValidationException;
import com.hororok.monta.service.PaletteService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import java.util.ArrayList;
import java.util.List;

@RestController
public class PaletteController {

    private final PaletteService paletteService;

    public PaletteController(PaletteService paletteService) {
        this.paletteService = paletteService;
    }

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
}
