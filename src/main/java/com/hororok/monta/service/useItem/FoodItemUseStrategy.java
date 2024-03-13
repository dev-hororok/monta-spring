package com.hororok.monta.service.useItem;

import com.hororok.monta.dto.response.FailResponseDto;
import com.hororok.monta.dto.response.itemInventory.UseFoodResponseDto;
import com.hororok.monta.entity.Character;
import com.hororok.monta.entity.CharacterInventory;
import com.hororok.monta.entity.ItemInventory;
import com.hororok.monta.entity.Member;
import com.hororok.monta.repository.CharacterInventoryRepository;
import com.hororok.monta.repository.CharacterRepository;
import com.hororok.monta.repository.ItemInventoryRepository;
import lombok.AllArgsConstructor;
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
public class FoodItemUseStrategy implements ItemUseStrategy{

    private CharacterRepository characterRepository;
    private CharacterInventoryRepository characterInventoryRepository;
    private ItemInventoryRepository itemInventoryRepository;

    @Autowired
    public FoodItemUseStrategy(CharacterRepository characterRepository, CharacterInventoryRepository characterInventoryRepository, ItemInventoryRepository itemInventoryRepository) {
        this.characterRepository = characterRepository;
        this.characterInventoryRepository = characterInventoryRepository;
        this.itemInventoryRepository = itemInventoryRepository;
    }

    @Override
    public ResponseEntity<?> useItem(ItemInventory itemInventory, Member member) {
        int effectCode = itemInventory.getItem().getEffectCode();

        // Progress가 0인지 체크
        if(itemInventory.getProgress() > 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new FailResponseDto(HttpStatus.BAD_REQUEST.name(), Collections.singletonList("잔여 시간만큼 공부해야 사용할 수 있습니다.")));
        }
        String characterGrade = characterGradeByEffectCode(effectCode);

        // 존재하지 않는 효과의 경우 : 운영자 문의 요청
        if(characterGrade.equals("Error")) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new FailResponseDto(HttpStatus.INTERNAL_SERVER_ERROR.name()
                            , Collections.singletonList("서버 오류 : 아이템 효과 없음 (운영자에게 문의해주세요)")));
        }

        // 구한 등급의 캐릭터 랜덤 뽑기
        Character character = randomCharacterByGrade(characterGrade);

        // character Inventory 저장
        Optional<CharacterInventory> findCharacterInventory = characterInventoryRepository.findOneByMemberIdAndCharacterId(member.getId(), character.getId());
        CharacterInventory saveCharacterInventory;

        if(findCharacterInventory.isEmpty()) {
            CharacterInventory characterInventory = new CharacterInventory(member, character, 1);
            saveCharacterInventory = characterInventoryRepository.save(characterInventory);
        } else {
            CharacterInventory characterInventory = findCharacterInventory.get();
            characterInventory.updateQuantity(characterInventory.getQuantity()+1);
            saveCharacterInventory = characterInventoryRepository.save(characterInventory);
        }

        // ItemInventory 수량 줄여 주고 soft delete
        itemInventory.updateQuantity(0);
        itemInventoryRepository.save(itemInventory);
        itemInventoryRepository.delete(itemInventory);

        return ResponseEntity.status(HttpStatus.OK)
                .body(new UseFoodResponseDto(saveCharacterInventory.getId(), character));
    }

    String characterGradeByEffectCode(int effectCode) {
        Random random = new Random();
        double randomValue = random.nextDouble();

        return switch (effectCode) {
            case 10000 -> "All";
            case 10001 -> randomValue < 0.65 ? "C" : (randomValue < 0.95 ? "B" : "A");
            case 10002 -> randomValue < 0.79 ? "C" : (randomValue < 0.99 ? "B" : "A");
            case 10003 -> randomValue < 0.79 ? "B" : (randomValue < 0.99 ? "A" : "A+");
            case 10004 -> randomValue < 0.90 ? "A" : "A+";
            case 10005 -> randomValue < 0.90 ? "A+" : "S";
            case 10006 -> randomValue < 0.90 ? "S" : "S+";
            case 10007 -> randomValue < 0.90 ? "S+" : "SS";
            default -> "Error";
        };
    }

    Character randomCharacterByGrade(String characterGrade) {
        List<Character> gradeCharacterList;

        if(characterGrade.equals("All")) {
            gradeCharacterList = characterRepository.findAll();
        } else {
            gradeCharacterList = characterRepository.findByGrade(characterGrade);
        }

        Random random = new Random();
        return gradeCharacterList.get(random.nextInt(gradeCharacterList.size()));
    }
}
