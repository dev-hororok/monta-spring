package com.hororok.monta.controller;

import com.hororok.monta.service.PaletteService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
