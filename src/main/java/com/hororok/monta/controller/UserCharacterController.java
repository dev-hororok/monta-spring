package com.hororok.monta.controller;

import com.hororok.monta.dto.response.CharacterInfoResponseDto;
import com.hororok.monta.dto.response.GetCharacterInfoByGradeResponseDto;
import com.hororok.monta.entity.GameCharacter;
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
import java.util.stream.Collectors;

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
            return ResponseEntity.ok(new GetCharacterInfoByGradeResponseDto(HttpStatus.OK.value(), new GetCharacterInfoByGradeResponseDto.Data(Collections.emptyList())));
        }

        List<GameCharacter> characters = characterService.getCharactersByGrade(grade);
        List<GetCharacterInfoByGradeResponseDto.Character> characterDtos = characters.stream()
                .map(character -> GetCharacterInfoByGradeResponseDto.Character.builder()
                        .characterId(character.getId())
                        .name(character.getName())
                        .description(character.getDescription())
                        .imageUrl(character.getImageUrl())
                        .grade(character.getGrade())
                        .sellPrice(character.getSellPrice())
                        .build())
                .collect(Collectors.toList());

        GetCharacterInfoByGradeResponseDto.Data data = new GetCharacterInfoByGradeResponseDto.Data(characterDtos);
        GetCharacterInfoByGradeResponseDto responseDto = new GetCharacterInfoByGradeResponseDto(HttpStatus.OK.value(), data);
        return ResponseEntity.ok(responseDto);
    }
}