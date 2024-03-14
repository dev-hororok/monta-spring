package com.hororok.monta.service.itemeffects;

import com.hororok.monta.dto.response.FailResponseDto;
import com.hororok.monta.entity.Character;
import com.hororok.monta.entity.CharacterInventory;
import com.hororok.monta.entity.ItemInventory;
import com.hororok.monta.entity.Member;
import com.hororok.monta.repository.CharacterInventoryRepository;
import com.hororok.monta.repository.CharacterRepository;
import com.hororok.monta.repository.ItemInventoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Component
public abstract class CharacterGacha {

    protected CharacterRepository characterRepository;
    protected CharacterInventoryRepository characterInventoryRepository;
    protected ItemInventoryRepository itemInventoryRepository;

    @Autowired
    public CharacterGacha(CharacterRepository characterRepository, CharacterInventoryRepository characterInventoryRepository, ItemInventoryRepository itemInventoryRepository) {
        this.characterRepository = characterRepository;
        this.characterInventoryRepository = characterInventoryRepository;
        this.itemInventoryRepository = itemInventoryRepository;
    }

    protected ResponseEntity<?> checkProgress(ItemInventory itemInventory) {
        if (itemInventory.getProgress() > 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new FailResponseDto(HttpStatus.BAD_REQUEST.name(), Collections.singletonList("잔여 시간만큼 공부해야 사용할 수 있습니다.")));
        }
        return null;
    }

    public Character randomCharacterByGrade(String characterGrade) {
        List<Character> gradeCharacterList;

        if(characterGrade.equals("All")) {
            gradeCharacterList = characterRepository.findAll();
        } else {
            gradeCharacterList = characterRepository.findByGrade(characterGrade);
        }

        Random random = new Random();
        return gradeCharacterList.get(random.nextInt(gradeCharacterList.size()));
    }

    public CharacterInventory saveOrUpdateCharacterInventory(Member member, Character character) {
        Optional<CharacterInventory> findCharacterInventory = characterInventoryRepository.findOneByMemberIdAndCharacterId(member.getId(), character.getId());
        if (findCharacterInventory.isEmpty()) {
            return characterInventoryRepository.save(new CharacterInventory(member, character, 1));
        } else {
            CharacterInventory existingInventory = findCharacterInventory.get();
            existingInventory.updateQuantity(existingInventory.getQuantity() + 1);
            return characterInventoryRepository.save(existingInventory);
        }
    }

    protected void deleteItemInventory(ItemInventory itemInventory) {
        itemInventory.updateQuantity(0);
        itemInventoryRepository.save(itemInventory);
        itemInventoryRepository.delete(itemInventory);
    }
}
