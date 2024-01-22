package com.hororok.monta.service;

import com.hororok.monta.dto.request.CreateCharacterRequestDto;
import com.hororok.monta.dto.request.PatchCharacterRequestDto;
import com.hororok.monta.entity.Character;
import com.hororok.monta.handler.CustomValidationException;
import com.hororok.monta.repository.CharacterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Service
public class CharacterService {
    private final CharacterRepository characterRepository;

    @Autowired
    public CharacterService(CharacterRepository characterRepository){
        this.characterRepository = characterRepository;
    }

    @Transactional
    public Character createCharacter(CreateCharacterRequestDto createCharacterRequestDto) {
        if(characterRepository.existsByName(createCharacterRequestDto.getName())) {
            throw new CustomValidationException(Collections.singletonList("이미 사용중인 캐릭터 이름입니다. 다른 이름을 입력해주세요."));
        }

        Character character = Character.builder()
                .name(createCharacterRequestDto.getName())
                .description(createCharacterRequestDto.getDescription())
                .imageUrl(createCharacterRequestDto.getImageUrl())
                .grade(createCharacterRequestDto.getGrade())
                .sellPrice(createCharacterRequestDto.getSellPrice())
                .build();

        return characterRepository.save(character);
    }

    @Transactional(readOnly = true)
    public List<Character> getAllCharacters(){
        return characterRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Character getCharacter(UUID characterId) {
        return characterRepository.findById(characterId).orElseThrow(() -> new CustomValidationException(Collections.singletonList("캐릭터를 찾을 수 없습니다.")));
    }

    @Transactional(readOnly = true)
    public List<Character> getCharactersByGrade(String grade) {
        return characterRepository.findByGrade(grade);
    }

    @Transactional
    public Character patchCharacter(UUID characterId, PatchCharacterRequestDto patchCharacterRequestDto) {
        Character character = characterRepository.findById(characterId)
                .orElseThrow(() -> new CustomValidationException(Collections.singletonList("캐릭터를 찾을 수 없습니다.")));

        character.setName(patchCharacterRequestDto.getName());
        character.setDescription(patchCharacterRequestDto.getDescription());
        character.setImageUrl(patchCharacterRequestDto.getImageUrl());
        character.setGrade(patchCharacterRequestDto.getGrade());
        character.setSellPrice(patchCharacterRequestDto.getSellPrice());

        return characterRepository.save(character);
    }

    @Transactional
    public void deleteCharacter(UUID characterId) {
        Character character = characterRepository.findById(characterId)
                .orElseThrow(() -> new CustomValidationException(Collections.singletonList("캐릭터를 찾을 수 없습니다.")));

        characterRepository.delete(character);
    }
}
