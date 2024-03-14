package com.hororok.monta.service.itemeffects.strategies;

import com.hororok.monta.dto.response.FailResponseDto;
import com.hororok.monta.dto.response.itemInventory.UseFoodResponseDto;
import com.hororok.monta.entity.Character;
import com.hororok.monta.entity.CharacterInventory;
import com.hororok.monta.entity.ItemInventory;
import com.hororok.monta.entity.Member;
import com.hororok.monta.repository.CharacterInventoryRepository;
import com.hororok.monta.repository.CharacterRepository;
import com.hororok.monta.repository.ItemInventoryRepository;
import com.hororok.monta.service.itemeffects.CharacterGacha;
import com.hororok.monta.service.itemeffects.EffectCode;
import com.hororok.monta.service.itemeffects.EffectCodeStrategy;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@EffectCode(10000)
@Component
public class CharacterGacha_10000 extends CharacterGacha implements EffectCodeStrategy {
    @Autowired
    public CharacterGacha_10000(CharacterRepository characterRepository,
                                CharacterInventoryRepository characterInventoryRepository,
                                ItemInventoryRepository itemInventoryRepository) {
        super(characterRepository, characterInventoryRepository, itemInventoryRepository);
    }

    @Override
    public ResponseEntity<?> useItem(ItemInventory itemInventory, Member member) {
        ResponseEntity<?> progressCheckResponse = checkProgress(itemInventory);
        if(progressCheckResponse != null) {
            return progressCheckResponse;
        }

        Character character = super.randomCharacterByGrade("All");
        CharacterInventory saveCharacterInventory = saveOrUpdateCharacterInventory(member, character);
        deleteItemInventory(itemInventory);

        return ResponseEntity.status(HttpStatus.OK)
                .body(new UseFoodResponseDto(saveCharacterInventory.getId(), character));
    }
}
