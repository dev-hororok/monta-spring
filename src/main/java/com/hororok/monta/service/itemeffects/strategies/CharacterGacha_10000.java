package com.hororok.monta.service.itemeffects.strategies;

import com.hororok.monta.dto.response.itemInventory.UseCharacterGachaResponseDto;
import com.hororok.monta.entity.Character;
import com.hororok.monta.entity.CharacterInventory;
import com.hororok.monta.entity.ItemInventory;
import com.hororok.monta.entity.Member;
import com.hororok.monta.repository.CharacterInventoryRepository;
import com.hororok.monta.repository.CharacterRepository;
import com.hororok.monta.repository.ItemInventoryRepository;
import com.hororok.monta.repository.TransactionRecordRepository;
import com.hororok.monta.service.itemeffects.CharacterGacha;
import com.hororok.monta.service.itemeffects.EffectCode;
import com.hororok.monta.service.itemeffects.EffectCodeStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

// Character 뽑기 (All random) - 도지코인 알 (Common 50%, Rare 30%, Epic 15%, Legendary 5%)
@EffectCode(10000)
@Component
public class CharacterGacha_10000 extends CharacterGacha implements EffectCodeStrategy {
    @Autowired
    public CharacterGacha_10000(CharacterRepository characterRepository, CharacterInventoryRepository characterInventoryRepository,
                                ItemInventoryRepository itemInventoryRepository, TransactionRecordRepository transactionRecordRepository) {
        super(characterRepository, characterInventoryRepository, itemInventoryRepository, transactionRecordRepository);
    }

    @Override
    public ResponseEntity<?> useItem(ItemInventory itemInventory, Member member) {
        // progress == 0 체크
        ResponseEntity<?> progressCheckResponse = checkProgress(itemInventory);
        if(progressCheckResponse != null) {
            return progressCheckResponse;
        }

        // 랜덤 캐릭터 뽑기
        Character character = randomCharacter();

        // Character Inventory 저장
        CharacterInventory saveCharacterInventory = saveOrUpdateCharacterInventory(member, character);

        // 사용한 item 삭제
        deleteItemInventory(itemInventory);

        // Transaction 기록
        recordTransaction(member, character);

        return ResponseEntity.status(HttpStatus.OK)
                .body(new UseCharacterGachaResponseDto(saveCharacterInventory.getId(), character));
    }
}
