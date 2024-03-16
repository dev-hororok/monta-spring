package com.hororok.monta.service.itemeffects.strategies;

import com.hororok.monta.dto.response.itemInventory.UsePaletteGachaResponseDto;
import com.hororok.monta.entity.ItemInventory;
import com.hororok.monta.entity.Member;
import com.hororok.monta.entity.Palette;
import com.hororok.monta.entity.StudyStreak;
import com.hororok.monta.repository.ItemInventoryRepository;
import com.hororok.monta.repository.PaletteRepository;
import com.hororok.monta.repository.StudyStreakRepository;
import com.hororok.monta.service.itemeffects.EffectCode;
import com.hororok.monta.service.itemeffects.EffectCodeStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.Random;

// Palette 뽑기 (Rare 60%, Epic 30%, Legendary 10%)
@EffectCode(20001)
@Component
public class PaletteGacha_20001 implements EffectCodeStrategy {
    private final ItemInventoryRepository itemInventoryRepository;
    private final StudyStreakRepository studyStreakRepository;
    private final PaletteRepository paletteRepository;

    @Autowired
    public PaletteGacha_20001(ItemInventoryRepository itemInventoryRepository, StudyStreakRepository studyStreakRepository, PaletteRepository paletteRepository) {
        this.itemInventoryRepository = itemInventoryRepository;
        this.studyStreakRepository = studyStreakRepository;
        this.paletteRepository = paletteRepository;
    }

    @Override
    public ResponseEntity<?> useItem(ItemInventory itemInventory, Member member) {
        // 랜덤 palette 추출
        Palette palette = randomPalette();

        // studyStreak update (존재하지 않으면 새로 만들어주고, 존재하면 Palette update)
        Optional<StudyStreak> findStudyStreak = studyStreakRepository.findByMemberId(member.getId());
        if(findStudyStreak.isEmpty()) {
            studyStreakRepository.save(new StudyStreak(palette, member));
        }
        else {
            StudyStreak studyStreak = findStudyStreak.get();
            studyStreak.updatePalette(palette);
            studyStreakRepository.save(studyStreak);
        }

        // ItemInventory 수량 줄이기
        itemInventory.updateQuantity(itemInventory.getQuantity() - 1);
        itemInventoryRepository.save(itemInventory);

        return ResponseEntity.status(HttpStatus.OK).body(new UsePaletteGachaResponseDto(palette));
    }

    public Palette randomPalette() {
        Random random = new Random();
        double randomValue = random.nextDouble();
        String grade;

        if(randomValue < 0.6) {
            grade = "Rare";
        } else if (randomValue < 0.9) {
            grade = "Epic";
        } else {
            grade = "Legendary";
        }

        List<Palette> paletteList = paletteRepository.findAllByGrade(grade);
        return paletteList.get(random.nextInt(paletteList.size()));
    }
}
