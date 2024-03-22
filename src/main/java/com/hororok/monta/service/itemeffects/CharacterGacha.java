package com.hororok.monta.service.itemeffects;

import com.hororok.monta.dto.response.FailResponseDto;
import com.hororok.monta.entity.*;
import com.hororok.monta.entity.Character;
import com.hororok.monta.repository.CharacterInventoryRepository;
import com.hororok.monta.repository.CharacterRepository;
import com.hororok.monta.repository.ItemInventoryRepository;
import com.hororok.monta.repository.TransactionRecordRepository;
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
    protected TransactionRecordRepository transactionRecordRepository;

    @Autowired
    public CharacterGacha(CharacterRepository characterRepository, CharacterInventoryRepository characterInventoryRepository,
                          ItemInventoryRepository itemInventoryRepository, TransactionRecordRepository transactionRecordRepository) {
        this.characterRepository = characterRepository;
        this.characterInventoryRepository = characterInventoryRepository;
        this.itemInventoryRepository = itemInventoryRepository;
        this.transactionRecordRepository = transactionRecordRepository;
    }

    protected ResponseEntity<?> checkProgress(ItemInventory itemInventory) {
        if (itemInventory.getProgress() > 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new FailResponseDto(HttpStatus.BAD_REQUEST.name(),
                            Collections.singletonList("잔여 시간만큼 공부해야 사용할 수 있습니다.")));
        }
        return null;
    }

    public Character randomCharacter() {
        Random random = new Random();
        double randomValue = random.nextDouble();
        String grade;

        if(randomValue < 0.5) {
            grade = "Common";
        } else if (randomValue < 0.8) {
            grade = "Rare";
        }
        else if (randomValue < 0.95) {
            grade = "Epic";
        } else {
            grade = "Legendary";
        }

        List<Character> characterList = characterRepository.findByGrade(grade);
        return characterList.get(random.nextInt(characterList.size()));
    }

    public CharacterInventory saveOrUpdateCharacterInventory(Member member, Character character) {
        Optional<CharacterInventory> findCharacterInventory = characterInventoryRepository.findOneByMemberIdAndCharacterId(member.getId(), character.getId());

        // 존재 하지 않으면 새로 저장, 존재 하면 수량 update
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

    public void recordTransaction(Member member, Character character) {
        transactionRecordRepository.save(new TransactionRecord(member, "Acquisition", 0,
                1, member.getPoint(), "Character 획득 : " + character.getName()));
    }
}
