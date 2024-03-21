package com.hororok.monta.utils;

import com.hororok.monta.dto.request.character.CreateCharacterRequestDto;
import com.hororok.monta.entity.Character;
import com.hororok.monta.repository.CharacterTestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CharacterUtils {
    @Autowired
    private CharacterTestRepository characterTestRepository;

    public CreateCharacterRequestDto createCharacter(boolean isRight) {
        if(isRight) {
            return new CreateCharacterRequestDto("Test Character " + Math.ceil(Math.random()*100), "테스트 캐릭터", "TestCharacterUrl", "B", 200);
        } else {
            return new CreateCharacterRequestDto("", "", "", "", 200);
        }
    }

    public Character saveCharacter(CreateCharacterRequestDto requestDto) {
        Character character = Character.builder()
                .name(requestDto.getName())
                .description(requestDto.getDescription())
                .imageUrl(requestDto.getImageUrl())
                .grade(requestDto.getGrade())
                .sellPrice(requestDto.getSellPrice())
                .build();
        return characterTestRepository.save(character);
    }

    public void deleteTestData(int characterId) {
        characterTestRepository.deleteTestData(characterId);
    }
}
