package com.hororok.monta.service;

import com.hororok.monta.dto.request.character.CreateCharacterRequestDto;
import com.hororok.monta.dto.request.character.UpdateCharacterRequestDto;
import com.hororok.monta.entity.Character;
import com.hororok.monta.handler.CustomValidationException;
import com.hororok.monta.repository.CharacterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class CharacterService {
    private final CharacterRepository characterRepository;

    @Autowired
    public CharacterService(CharacterRepository characterRepository){
        this.characterRepository = characterRepository;
    }

    @Transactional
    public Character addCharacterDetails(CreateCharacterRequestDto createCharacterRequestDto) {
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
    public List<Character> findAllCharacterList(){
        return characterRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Character findCharacterDetails(int characterId) {
        return characterRepository.findById(characterId).orElseThrow(() -> new CustomValidationException(Collections.singletonList("캐릭터를 찾을 수 없습니다.")));
    }

    @Transactional(readOnly = true)
    public List<Character> findCharacterByGradeList(String grade) {
        return characterRepository.findByGrade(grade);
    }

    @Transactional
    public Character updateCharacterDetails(int characterId, UpdateCharacterRequestDto requestDto) {
        Character character = characterRepository.findById(characterId)
                .orElseThrow(() -> new CustomValidationException(Collections.singletonList("캐릭터를 찾을 수 없습니다.")));

        List<UpdateCharacterRequestDto> list = new ArrayList<>();
        list.add(requestDto);

        for (UpdateCharacterRequestDto characterRequestDto : list) {
            if (characterRequestDto.getName() != null) character.setName(characterRequestDto.getName());
            if (characterRequestDto.getDescription() != null)
                character.setDescription(characterRequestDto.getDescription());
            if (characterRequestDto.getImageUrl() != null) character.setImageUrl(characterRequestDto.getImageUrl());
            if (characterRequestDto.getGrade() != null) character.setGrade(characterRequestDto.getGrade());
            if (characterRequestDto.getSellPrice() != null) character.setSellPrice(characterRequestDto.getSellPrice());
        }

        return characterRepository.save(character);
    }

    @Transactional
    public void deleteCharacter(int characterId) {
        Character character = characterRepository.findById(characterId)
                .orElseThrow(() -> new CustomValidationException(Collections.singletonList("캐릭터를 찾을 수 없습니다.")));

        characterRepository.delete(character);
    }
}
