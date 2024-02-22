package com.hororok.monta.service;

import com.hororok.monta.dto.request.item.UpdateItemRequestDto;
import com.hororok.monta.dto.request.item.CreateItemRequestDto;
import com.hororok.monta.dto.response.DeleteResponseDto;
import com.hororok.monta.dto.response.FailResponseDto;
import com.hororok.monta.dto.response.item.GetItemResponseDto;
import com.hororok.monta.dto.response.item.GetItemsResponseDto;
import com.hororok.monta.dto.response.item.PatchItemResponseDto;
import com.hororok.monta.dto.response.item.PostItemResponseDto;
import com.hororok.monta.dto.response.itemInventory.UseConsumableResponseDto;
import com.hororok.monta.dto.response.itemInventory.UseFoodResponseDto;
import com.hororok.monta.entity.*;
import com.hororok.monta.entity.Character;
import com.hororok.monta.repository.*;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
@AllArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;
    private final ItemInventoryRepository itemInventoryRepository;
    private final CharacterRepository characterRepository;
    private final CharacterInventoryRepository characterInventoryRepository;
    private final PaletteRepository paletteRepository;
    private final StudyStreakRepository studyStreakRepository;
    private final MemberService memberService;

    @Transactional
    public ResponseEntity<?> postItem(CreateItemRequestDto requestDto) {
        Item saveItem = itemRepository.save(new Item(requestDto));
        return ResponseEntity.status(HttpStatus.CREATED).body(new PostItemResponseDto(saveItem.getId()));
    }

    @Transactional
    public ResponseEntity<?> getItems() {
        List<Item> items = itemRepository.findAll();
        return ResponseEntity.status(HttpStatus.OK).body(new GetItemsResponseDto(items));
    }

    @Transactional
    public ResponseEntity<?> getItem(int itemId) {
        Optional<Item> findItem = itemRepository.findOneById(itemId);

        if(findItem.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new FailResponseDto(HttpStatus.NOT_FOUND.name(), Collections.singletonList("존재하지 않는 아이템입니다.")));
        }
        Item item = findItem.get();
        return ResponseEntity.status(HttpStatus.OK).body(new GetItemResponseDto(item));
    }

    @Transactional
    public ResponseEntity<?> patchItem(UpdateItemRequestDto requestDto, int itemId) {

        Optional<Item> findItem = itemRepository.findOneById(itemId);

        // 아이템 존재 여부 점검
        if(findItem.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new FailResponseDto(HttpStatus.NOT_FOUND.name(), Collections.singletonList("존재하지 않는 아이템입니다.")));
        }

        // 빈 값이 아니면 새로운 값으로 수정
        Item item = findItem.get();

        String itemType = requestDto.getItemType();
        String name = requestDto.getName();
        String grade = requestDto.getGrade();
        String description = requestDto.getDescription();
        String imageUrl = requestDto.getImageUrl();
        Integer cost = requestDto.getCost();
        Integer requiredStudyTime = requestDto.getRequiredStudyTime();
        Integer effectCode = requestDto.getEffectCode();
        Boolean isHidden = requestDto.getIsHidden();

        if(requestDto.getItemType().isBlank()) itemType = item.getItemType();
        if(requestDto.getName().isBlank()) name = item.getName();
        if(requestDto.getGrade().isBlank()) grade = item.getGrade();
        if(requestDto.getDescription().isBlank()) description = item.getDescription();
        if(requestDto.getImageUrl().isBlank()) imageUrl = item.getImageUrl();
        if(requestDto.getCost()==null) cost = item.getCost();
        if(requestDto.getRequiredStudyTime()==null) requiredStudyTime = item.getRequiredStudyTime();
        if(requestDto.getEffectCode()==null) effectCode = item.getEffectCode();
        if(requestDto.getIsHidden()==null) isHidden = item.getIsHidden();


        // DB 수정
        item.updateItem(itemType, name, grade, description, imageUrl, cost, requiredStudyTime, effectCode, isHidden);
        Item updateItem = itemRepository.save(item);
        return ResponseEntity.status(HttpStatus.OK).body(new PatchItemResponseDto(updateItem));
    }

    @Transactional
    public ResponseEntity<?> deleteItem(int itemId) {

        Optional<Item> findItem = itemRepository.findOneById(itemId);

        // 아이템 존재 여부 점검
        if(findItem.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new FailResponseDto(HttpStatus.NOT_FOUND.name(), Collections.singletonList("존재하지 않는 아이템입니다.")));
        }

        // DB에서 삭제
        itemRepository.delete(findItem.get());
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(new DeleteResponseDto());
    }

    public ResponseEntity<?> useItem(Long itemInventoryId) {

        // Member 정보 추출
        Optional<Member> findMember = memberService.findMember(memberService.getMemberAccountId());
        if(findMember.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new FailResponseDto(HttpStatus.NOT_FOUND.name(), Collections.singletonList("존재하지 않는 유저 입니다.")));
        }
        Member member = findMember.get();

        // 유효한, 본인이 가진 itemInventoryId 인지 체크
        Optional<ItemInventory> findItemInventory = itemInventoryRepository.findByIdAndMemberId(itemInventoryId, member.getId());
        if(findItemInventory.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new FailResponseDto(HttpStatus.NOT_FOUND.name(), Collections.singletonList("소유하지 않은 item 입니다.")));
        }
        ItemInventory itemInventory = findItemInventory.get();

        // quantity 수량 남아 있는지 확인
        if(itemInventory.getQuantity()<=0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new FailResponseDto(HttpStatus.BAD_REQUEST.name(), Collections.singletonList("사용할 수 있는 수량이 없습니다.")));
        }

        // 효과 번호 추출
        int effectCode = itemInventory.getItem().getEffectCode();

        // 10000번대일 (Food) 경우
        if(effectCode>=10000 && effectCode<20000) {
            // Progress가 0인지 체크
            if(itemInventory.getProgress()>0) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new FailResponseDto(HttpStatus.BAD_REQUEST.name(), Collections.singletonList("잔여 시간만큼 공부해야 사용할 수 있습니다.")));
            }
            String characterGrade = characterGradeByEffect(effectCode);

            // 존재하지 않는 효과의 경우 : 운영자 문의 요청
            if(characterGrade.equals("Error")) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(new FailResponseDto(HttpStatus.INTERNAL_SERVER_ERROR.name()
                                , Collections.singletonList("서버 오류 : 아이템 효과 없음 (운영자에게 문의해주세요)")));
            }

            // 구한 등급의 캐릭터 랜덤 뽑기
            List<Character> gradeCharacterList = characterRepository.findByGrade(characterGrade);
            if(gradeCharacterList.isEmpty()) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(new FailResponseDto(HttpStatus.INTERNAL_SERVER_ERROR.name(),
                                Collections.singletonList("서버 오류 : 해당 등급의 캐릭터 없음 (운영자에게 문의해주세요.)")));
            }
            Character character = randomCharacterByGrade(gradeCharacterList);


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

        // 20000번대일 (Consumable) 경우
        if(effectCode>=20000 && effectCode<30000) {

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

            return ResponseEntity.status(HttpStatus.OK)
                    .body(new UseConsumableResponseDto(palette));
        }

        // 이외의 이상이 발생하는 경우
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new FailResponseDto(HttpStatus.INTERNAL_SERVER_ERROR.name()
                        , Collections.singletonList("서버 오류 : 운영자에게 문의해주세요.")));
    }


    public String characterGradeByEffect(int effectCode) {
        Random random = new Random();
        double randomValue = random.nextDouble();

        return switch (effectCode) {
            case 10000 -> randomValue < 0.65 ? "C" : (randomValue < 0.95 ? "B" : "A");
            case 10001 -> randomValue < 0.79 ? "C" : (randomValue < 0.99 ? "B" : "A");
            case 10002 -> randomValue < 0.79 ? "B" : (randomValue < 0.99 ? "A" : "A+");
            case 10003 -> randomValue < 0.90 ? "A" : "A+";
            case 10004 -> randomValue < 0.90 ? "A+" : "S";
            case 10005 -> randomValue < 0.90 ? "S" : "S+";
            case 10006 -> randomValue < 0.90 ? "S+" : "SS";
            default -> "Error";
        };
    }

    public Character randomCharacterByGrade(List<Character> characterList) {
        Random random = new Random();
        return characterList.get(random.nextInt(characterList.size()));
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
