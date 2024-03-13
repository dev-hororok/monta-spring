package com.hororok.monta.service.useItem;

import com.hororok.monta.dto.response.FailResponseDto;
import com.hororok.monta.dto.response.itemInventory.UseConsumableResponseDto;
import com.hororok.monta.entity.ItemInventory;
import com.hororok.monta.entity.Member;
import com.hororok.monta.entity.Palette;
import com.hororok.monta.entity.StudyStreak;
import com.hororok.monta.repository.ItemInventoryRepository;
import com.hororok.monta.repository.PaletteRepository;
import com.hororok.monta.repository.StudyStreakRepository;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@NoArgsConstructor
@Component
public class ConsumableItemUseStrategy implements ItemUseStrategy{

    private ItemInventoryRepository itemInventoryRepository;
    private StudyStreakRepository studyStreakRepository;
    private PaletteRepository paletteRepository;

    @Autowired
    public ConsumableItemUseStrategy(ItemInventoryRepository itemInventoryRepository, StudyStreakRepository studyStreakRepository, PaletteRepository paletteRepository) {
        this.itemInventoryRepository = itemInventoryRepository;
        this.studyStreakRepository = studyStreakRepository;
        this.paletteRepository = paletteRepository;
    }

    @Override
    public ResponseEntity<?> useItem(ItemInventory itemInventory, Member member) {

        // 효과 번호 추출
        int effectCode = itemInventory.getItem().getEffectCode();

        // 존재하지 않는 효과의 경우 : 운영자 문의 요청
        Palette palette = consumableEffect(effectCode);
        if(palette==null) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new FailResponseDto(HttpStatus.INTERNAL_SERVER_ERROR.name()
                            , Collections.singletonList("서버 오류 : 아이템 효과 없음 (운영자에게 문의해주세요.)")));
        }

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

        // ItemInventory 수량 줄여 주기
        itemInventory.updateQuantity(itemInventory.getQuantity()-1);
        itemInventoryRepository.save(itemInventory);

        return ResponseEntity.status(HttpStatus.OK).body(new UseConsumableResponseDto(palette));
    }

    public Palette consumableEffect(int effectCode) {
        return switch (effectCode) {
            case 20000 -> randomPaletteByEffect();
            default -> null;
        };
    }

    public Palette randomPaletteByEffect() {
        Random random = new Random();
        List<Palette> paletteList = paletteRepository.findAll();
        return paletteList.get(random.nextInt(paletteList.size()));
    }
}
