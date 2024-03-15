package com.hororok.monta.service.itemeffects.strategies;

import com.hororok.monta.dto.response.FailResponseDto;
import com.hororok.monta.entity.ItemInventory;
import com.hororok.monta.entity.Member;
import com.hororok.monta.repository.CharacterInventoryRepository;
import com.hororok.monta.repository.CharacterRepository;
import com.hororok.monta.repository.ItemInventoryRepository;
import com.hororok.monta.service.itemeffects.CharacterGacha;
import com.hororok.monta.service.itemeffects.EffectCode;
import com.hororok.monta.service.itemeffects.EffectCodeStrategy;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.Collections;

@EffectCode(99999)
@Component
public class Else extends CharacterGacha implements EffectCodeStrategy {
    public Else(CharacterRepository characterRepository, CharacterInventoryRepository characterInventoryRepository, ItemInventoryRepository itemInventoryRepository) {
        super(characterRepository, characterInventoryRepository, itemInventoryRepository);
    }

    @Override
    public ResponseEntity<?> useItem(ItemInventory itemInventory, Member member) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new FailResponseDto(HttpStatus.INTERNAL_SERVER_ERROR.name()
                        , Collections.singletonList("서버 오류 : 아이템 효과 없음 (운영자에게 문의해주세요)")));
    }
}
