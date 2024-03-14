package com.hororok.monta.service;

import com.hororok.monta.dto.request.shop.PurchaseRequestDto;
import com.hororok.monta.dto.request.shop.SellRequestDto;
import com.hororok.monta.dto.response.FailResponseDto;
import com.hororok.monta.dto.response.shop.TransactionResponseDto;
import com.hororok.monta.entity.*;
import com.hororok.monta.entity.Character;
import com.hororok.monta.repository.*;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@AllArgsConstructor
public class ShopService {
    private final ItemRepository itemRepository;
    private final ItemInventoryRepository itemInventoryRepository;
    private final TransactionRecordRepository transactionRecordRepository;
    private final MemberRepository memberRepository;
    private final CharacterRepository characterRepository;
    private final CharacterInventoryRepository characterInventoryRepository;
    private final MemberService memberService;

    @Transactional
    public ResponseEntity<?> addPurchaseDetails(PurchaseRequestDto requestDto) {

        // item_id 존재하는지 체크
        int itemId = requestDto.getItemId();
        Optional<Item> findItem = itemRepository.findOneById(itemId);

        if(findItem.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new FailResponseDto(HttpStatus.NOT_FOUND.name(), Collections.singletonList("존재하지 않는 Item 입니다.")));
        }
        Item item = findItem.get();

        // Member 정보 추출 (보유한 point, Food 갯수 파악 위함)
        Optional<Member> findMember = memberService.findMemberDetails(memberService.findMemberAccountId());

        if(findMember.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new FailResponseDto(HttpStatus.NOT_FOUND.name(), Collections.singletonList("존재하지 않는 유저 입니다.")));
        }
        Member member = findMember.get();

        // 수량 0 이상인지 체크
        if(requestDto.getCount()<=0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new FailResponseDto(HttpStatus.BAD_REQUEST.name(), Collections.singletonList("수량을 1개 이상 선택해주세요.")));
        }

        // 구매할 포인트가 있는지 체크 (아이템*count < point)
        int purchaseCost = item.getCost() * requestDto.getCount();

        if(purchaseCost > member.getPoint()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new FailResponseDto(HttpStatus.BAD_REQUEST.name(), Collections.singletonList("point가 부족합니다.")));
        }

        // item_type = "Food"
        if(Objects.equals(item.getItemType(), "Food")) {
            // 보유 갯수가 4개 미만인지 체크
            int existingFoodCount = itemInventoryRepository.countByMemberIdAndItemType(member.getId(), "Food");

            if (existingFoodCount + requestDto.getCount() > 4) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new FailResponseDto(HttpStatus.BAD_REQUEST.name()
                                , Collections.singletonList("음식은 최대 4개까지 보유할 수 있습니다.")));
            }
        }

        // TransactionRecord 저장
        int point = member.getPoint() - purchaseCost;
        TransactionRecord transactionRecord = saveTransactionRecord(member, "Purchase", purchaseCost,
                requestDto.getCount(), point, item.getName() + " " + requestDto.getCount() + "개 구매");

        // ItemInventory 저장
        if(Objects.equals(item.getItemType(), "Food")) {
            for(int i=0 ; i<requestDto.getCount() ; i++) {
                itemInventoryRepository.save(new ItemInventory(item, member, 1));
            }
        }
        else if(Objects.equals(item.getItemType(), "Consumable")) {
            Optional<ItemInventory> findConsumable = itemInventoryRepository.findByMemberIdAndItemType(member.getId(), "Consumable");

            // 기존 consumable 없을 경우 새로 추가
            if(findConsumable.isEmpty()) {
                itemInventoryRepository.save(new ItemInventory(item, member, requestDto.getCount()));
            }
            // 기존 consumable 있을 경우 수량만 추가
            else {
                ItemInventory consumableInventory = findConsumable.get();
                consumableInventory.updateQuantity(consumableInventory.getQuantity()+requestDto.getCount());
                itemInventoryRepository.save(consumableInventory);
            }
        }

        // Member point 저장
        member.updatePoint(point);
        memberRepository.save(member);

        // 성공 반환
        return ResponseEntity.status(HttpStatus.CREATED).body(new TransactionResponseDto(transactionRecord));
    }

    @Transactional
    public ResponseEntity<?> addSellDetails(SellRequestDto requestDtoV2) {

        // 판매 수량 0 이상인지 체크
        int sellQuantity = requestDtoV2.getCount();
        if(sellQuantity <= 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new FailResponseDto(HttpStatus.BAD_REQUEST.name(), Collections.singletonList("수량을 1개 이상 선택해주세요.")));
        }

        // Member 정보 추출
        Optional<Member> findMember = memberService.findMemberDetails(memberService.findMemberAccountId());
        if(findMember.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new FailResponseDto(HttpStatus.NOT_FOUND.name(), Collections.singletonList("존재하지 않는 유저 입니다.")));
        }
        Member member = findMember.get();

        // character_inventory_id 존재 여부 체크
        long characterInventoryId = requestDtoV2.getCharacterInventoryId();
        Optional<CharacterInventory> findCharacterInventory = characterInventoryRepository.findById(characterInventoryId);
        if (findCharacterInventory.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new FailResponseDto(HttpStatus.NOT_FOUND.name(), Collections.singletonList("보유하고 있는 캐릭터로 판매 요청해주세요.")));
        }
        CharacterInventory characterInventory = findCharacterInventory.get();

        // 본인이 소유한 인벤토리인지 체크
        if (!characterInventory.getMember().getId().equals(member.getId())) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new FailResponseDto(HttpStatus.NOT_FOUND.name(), Collections.singletonList("보유하고 있는 캐릭터로 판매 요청해주세요.")));
        }

        // 선택한 수량 만큼 가지고 있는지 체크 (inventory 점검)
        if(characterInventory.getQuantity() < sellQuantity) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new FailResponseDto(HttpStatus.BAD_REQUEST.name(), Collections.singletonList("Inventory에 소유한 수량을 초과하였습니다.")));
        }

        // Character 정보 추출
        Optional<Character> findCharacter = characterRepository.findById(characterInventory.getCharacter().getId());
        if(findCharacter.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new FailResponseDto(HttpStatus.NOT_FOUND.name(), Collections.singletonList("존재하지 않는 캐릭터 입니다.")));
        }
        Character character = findCharacter.get();

        // TransactionRecord 저장
        int sellPrice = character.getSellPrice() * sellQuantity;
        int point = member.getPoint() + sellPrice;
        TransactionRecord transactionRecord = saveTransactionRecord(member, "Sell", sellPrice,
                sellQuantity, point, character.getName() + " " + sellQuantity + "개 판매");

        // character Inventory 저장
        characterInventory.updateQuantity(characterInventory.getQuantity() - sellQuantity);
        characterInventoryRepository.save(characterInventory);

        // Member point 저장
        member.updatePoint(point);
        memberRepository.save(member);

        // 성공 반환
        return ResponseEntity.status(HttpStatus.CREATED).body(new TransactionResponseDto(transactionRecord));
    }

    // 거래 내역 저장 메서드
    public TransactionRecord saveTransactionRecord(Member member, String type, int amount, int count, int point, String notes) {
        return transactionRecordRepository.save(new TransactionRecord(member, type, amount, count, point, notes));
    }
}
