package com.hororok.monta.utils;

import com.hororok.monta.entity.Character;
import com.hororok.monta.entity.CharacterInventory;
import com.hororok.monta.entity.Member;
import com.hororok.monta.repository.CharacterInventoryTestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CharacterInventoryUtils {
    @Autowired
    private CharacterInventoryTestRepository characterInventoryTestRepository;

    public CharacterInventory saveCharacterInventory(Member member, Character character) {
        return characterInventoryTestRepository.save(new CharacterInventory(member, character, 1));
    }

    public void deleteTestData(long characterInventoryId) {
        characterInventoryTestRepository.deleteTestData(characterInventoryId);
    }
}
