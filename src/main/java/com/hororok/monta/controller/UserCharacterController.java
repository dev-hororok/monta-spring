package com.hororok.monta.controller;

import com.hororok.monta.dto.response.character.GetCharacterByGradeResponseDto;
import com.hororok.monta.entity.Character;
import com.hororok.monta.service.CharacterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/characters")
public class UserCharacterController {
    private final CharacterService characterService;

    @Autowired
    private UserCharacterController(CharacterService characterService){
        this.characterService = characterService;
    }

    @GetMapping("")
    public ResponseEntity<?> getCharactersByGrade(@RequestParam(required = false) String grade) {
        if (grade == null) {
            return ResponseEntity.ok(new GetCharacterByGradeResponseDto("success", new GetCharacterByGradeResponseDto.Data(Collections.emptyList())));
        }
        List<Character> characters = characterService.getCharactersByGrade(grade);
        return ResponseEntity.status(HttpStatus.OK).body(new GetCharacterByGradeResponseDto(characters));
    }
}
