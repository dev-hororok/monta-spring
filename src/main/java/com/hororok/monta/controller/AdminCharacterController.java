package com.hororok.monta.controller;

import com.hororok.monta.dto.request.character.CreateCharacterRequestDto;
import com.hororok.monta.dto.request.character.PatchCharacterRequestDto;
import com.hororok.monta.dto.response.character.AllCharactersInfoResponseDto;
import com.hororok.monta.dto.response.character.BasicCharacterResponseDto;
import com.hororok.monta.dto.response.character.CharacterResponseDto;
import com.hororok.monta.dto.response.character.CharacterInfoResponseDto;
import com.hororok.monta.entity.Character;
import com.hororok.monta.handler.CustomValidationException;
import com.hororok.monta.service.CharacterService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/admin/characters")
public class AdminCharacterController {
    private final CharacterService characterService;

    @Autowired
    public AdminCharacterController(CharacterService characterService){
        this.characterService = characterService;
    }

    @PostMapping("")
    public ResponseEntity<?> postCharacter(@Valid @RequestBody CreateCharacterRequestDto createCharacterRequestDto, BindingResult bindingResult){
        checkValidationErrors(bindingResult);

        Character savedCharacter = characterService.createCharacter(createCharacterRequestDto);
        CharacterResponseDto responseDto = new CharacterResponseDto();
        responseDto.setCharacterId(savedCharacter.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    @GetMapping("")
    public ResponseEntity<?> getAllCharacters(){
        List<Character> characters = characterService.getAllCharacters();
        List<AllCharactersInfoResponseDto.Character> characterDtos = characters.stream().map(c -> AllCharactersInfoResponseDto.Character.builder()
                .characterId(c.getId())
                .name(c.getName())
                .description(c.getDescription())
                .imageUrl(c.getImageUrl())
                .grade(c.getGrade())
                .sellPrice(c.getSellPrice())
                .createdAt(c.getCreatedAt())
                .updatedAt(c.getUpdatedAt())
                .build()).collect(Collectors.toList());

        AllCharactersInfoResponseDto.Data data = new AllCharactersInfoResponseDto.Data(characterDtos);
        AllCharactersInfoResponseDto responseDto = new AllCharactersInfoResponseDto("success", data);
        return ResponseEntity.ok(responseDto);
    }

    @GetMapping("/{characterId}")
    public ResponseEntity<?> getCharacter(@PathVariable UUID characterId){
        Character character = characterService.getCharacter(characterId);
        CharacterInfoResponseDto.Character characterDto = CharacterInfoResponseDto.Character.builder()
                .characterId(character.getId())
                .name(character.getName())
                .description(character.getDescription())
                .imageUrl(character.getImageUrl())
                .grade(character.getGrade())
                .sellPrice(character.getSellPrice())
                .createdAt(character.getCreatedAt())
                .updatedAt(character.getUpdatedAt())
                .build();

        CharacterInfoResponseDto.Data data = new CharacterInfoResponseDto.Data(characterDto);
        CharacterInfoResponseDto responseDto = new CharacterInfoResponseDto("success", data);
        return ResponseEntity.ok(responseDto);
    }

    @PatchMapping("/{characterId}")
    public ResponseEntity<?> patchCharacter(@PathVariable UUID characterId, @Valid @RequestBody PatchCharacterRequestDto patchCharacterRequestDto, BindingResult bindingResult){
        checkValidationErrors(bindingResult);
        Character updatedCharacter = characterService.patchCharacter(characterId, patchCharacterRequestDto);

        CharacterInfoResponseDto.Character characterDto = convertToCharacterInfoResponseDto(updatedCharacter);

        CharacterInfoResponseDto.Data data = new CharacterInfoResponseDto.Data(characterDto);
        CharacterInfoResponseDto responseDto = new CharacterInfoResponseDto("success", data);
        return ResponseEntity.ok(responseDto);
    }

    @DeleteMapping("/{characterId}")
    public ResponseEntity<?> deleteCharacter(@PathVariable UUID characterId){
        characterService.deleteCharacter(characterId);
        BasicCharacterResponseDto responseDto = BasicCharacterResponseDto.builder()
                .status("success")
                .data(null)
                .build();

        return ResponseEntity.ok(responseDto);
    }

    private void checkValidationErrors(BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            List<String> errors = new ArrayList<>();
            for (FieldError error : bindingResult.getFieldErrors()) {
                errors.add(error.getField() + " : " + error.getDefaultMessage());
            }
            throw new CustomValidationException(errors);
        }
    }

    private CharacterInfoResponseDto.Character convertToCharacterInfoResponseDto(Character character) {
        return CharacterInfoResponseDto.Character.builder()
                .characterId(character.getId())
                .name(character.getName())
                .description(character.getDescription())
                .imageUrl(character.getImageUrl())
                .grade(character.getGrade())
                .sellPrice(character.getSellPrice())
                .createdAt(character.getCreatedAt())
                .updatedAt(character.getUpdatedAt())
                .build();
    }
}