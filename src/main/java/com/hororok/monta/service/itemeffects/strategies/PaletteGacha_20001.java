package com.hororok.monta.service.itemeffects.strategies;

import com.hororok.monta.dto.response.itemInventory.UsePaletteGachaResponseDto;
import com.hororok.monta.entity.*;
import com.hororok.monta.repository.ItemInventoryRepository;
import com.hororok.monta.repository.PaletteRepository;
import com.hororok.monta.repository.StudyStreakRepository;
import com.hororok.monta.repository.TransactionRecordRepository;
import com.hororok.monta.service.itemeffects.EffectCode;
import com.hororok.monta.service.itemeffects.EffectCodeStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.Random;

// Palette 뽑기 (Common 58%, Rare 30%, Epic 10%, Legendary 2%)
@EffectCode(20001)
@Component
public class PaletteGacha_20001 implements EffectCodeStrategy {
    private final ItemInventoryRepository itemInventoryRepository;
    private final StudyStreakRepository studyStreakRepository;
    private final PaletteRepository paletteRepository;
    private final TransactionRecordRepository transactionRecordRepository;

    @Autowired
    public PaletteGacha_20001(ItemInventoryRepository itemInventoryRepository, StudyStreakRepository studyStreakRepository,
                              PaletteRepository paletteRepository, TransactionRecordRepository transactionRecordRepository) {
        this.itemInventoryRepository = itemInventoryRepository;
        this.studyStreakRepository = studyStreakRepository;
        this.paletteRepository = paletteRepository;
        this.transactionRecordRepository = transactionRecordRepository;
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

        // Transaction 기록
        recordTransaction(member, palette);

        return ResponseEntity.status(HttpStatus.OK).body(new UsePaletteGachaResponseDto(palette));
    }

    public Palette randomPalette() {
        Random random = new Random();
        double randomValue = random.nextDouble();
        String grade;

        if(randomValue < 0.58) {
            grade = "Common";
        } else if (randomValue < 0.88) {
            grade = "Rare";
        }
        else if (randomValue < 0.98) {
            grade = "Epic";
        } else {
            grade = "Legendary";
        }

        List<Palette> paletteList = paletteRepository.findAllByGrade(grade);
        return paletteList.get(random.nextInt(paletteList.size()));
    }

    public void recordTransaction(Member member, Palette palette) {
        transactionRecordRepository.save(new TransactionRecord(member, "Acquisition", 0,
                1, member.getPoint(), "Palette 획득 : " + palette.getName()));
    }
}
