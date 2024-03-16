package com.hororok.monta.service.itemeffects.strategies;

import com.hororok.monta.entity.ItemInventory;
import com.hororok.monta.entity.Member;
import com.hororok.monta.repository.ItemInventoryRepository;
import com.hororok.monta.service.itemeffects.EffectCode;
import com.hororok.monta.service.itemeffects.EffectCodeStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.Random;

// All 랜덤 뽑기 (Character, Streak, Point)
@EffectCode(90000)
@Component
public class AllGacha_90000 implements EffectCodeStrategy {
    private final CharacterGacha_10000 characterGacha10000;
    private final PaletteGacha_20000 streakGacha20000;
    private final PointBoxGacha_30000 pointBoxGacha30000;

    @Autowired
    public AllGacha_90000(CharacterGacha_10000 characterGacha10000
            , PaletteGacha_20000 streakGacha20000, PointBoxGacha_30000 pointBoxGacha30000) {
        this.characterGacha10000 = characterGacha10000;
        this.streakGacha20000 = streakGacha20000;
        this.pointBoxGacha30000 = pointBoxGacha30000;
    }

    @Override
    public ResponseEntity<?> useItem(ItemInventory itemInventory, Member member) {
        Random random = new Random();
        double randomValue = random.nextDouble();

        if(randomValue < 0.33) {
            return characterGacha10000.useItem(itemInventory, member);
        } else if (randomValue < 0.66) {
            return streakGacha20000.useItem(itemInventory, member);
        } else {
            return pointBoxGacha30000.useItem(itemInventory, member);
        }
    }
}
