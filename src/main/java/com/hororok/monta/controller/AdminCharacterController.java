package com.hororok.monta.controller;

import com.hororok.monta.dto.request.character.CreateCharacterRequestDto;
import com.hororok.monta.dto.request.character.UpdateCharacterRequestDto;
import com.hororok.monta.dto.response.DeleteResponseDto;
import com.hororok.monta.dto.response.character.GetCharactersResponseDto;
import com.hororok.monta.dto.response.character.CreateCharacterResponseDto;
import com.hororok.monta.dto.response.character.GetCharacterResponseDto;
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

@RestController
@RequestMapping("/admin/characters")
public class AdminCharacterController {
    private final CharacterService characterService;

    @Autowired
    public AdminCharacterController(CharacterService characterService){
        this.characterService = characterService;
    }

    @PostMapping("")
    public ResponseEntity<?> postCharacterDetails(@Valid @RequestBody CreateCharacterRequestDto createCharacterRequestDto, BindingResult bindingResult){
        checkValidationErrors(bindingResult);

        Character savedCharacter = characterService.addCharacterDetails(createCharacterRequestDto);
        CreateCharacterResponseDto responseDto = new CreateCharacterResponseDto(savedCharacter.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    @GetMapping("")
    public ResponseEntity<?> getCharacterList(){
        List<Character> characters = characterService.findAllCharacterList();
        return ResponseEntity.status(HttpStatus.OK).body(new GetCharactersResponseDto(characters));
    }

    @GetMapping("/{characterId}")
    public ResponseEntity<?> getCharacterDetails(@PathVariable int characterId){
        Character character = characterService.findCharacterDetails(characterId);
        return ResponseEntity.status(HttpStatus.OK).body(new GetCharacterResponseDto(character));
    }

    @PatchMapping("/{characterId}")
    public ResponseEntity<?> patchCharacterDetails(@PathVariable int characterId, @Valid @RequestBody UpdateCharacterRequestDto updateCharacterRequestDto, BindingResult bindingResult){
        checkValidationErrors(bindingResult);
        Character updatedCharacter = characterService.updateCharacterDetails(characterId, updateCharacterRequestDto);
        return ResponseEntity.status(HttpStatus.OK).body(new GetCharacterResponseDto(updatedCharacter));
    }

    @DeleteMapping("/{characterId}")
    public ResponseEntity<?> deleteCharacter(@PathVariable int characterId){
        characterService.deleteCharacter(characterId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(new DeleteResponseDto());
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
}
