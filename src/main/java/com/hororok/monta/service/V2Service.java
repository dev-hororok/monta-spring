package com.hororok.monta.service;

import com.hororok.monta.dto.request.item.PatchItemRequestDto;
import com.hororok.monta.dto.request.item.PostItemRequestDto;
import com.hororok.monta.dto.request.shop.PurchaseRequestDtoV2;
import com.hororok.monta.dto.response.DeleteResponseDto;
import com.hororok.monta.dto.response.FailResponseDto;
import com.hororok.monta.dto.response.item.GetItemResponseDto;
import com.hororok.monta.dto.response.item.GetItemsResponseDto;
import com.hororok.monta.dto.response.item.PatchItemResponseDto;
import com.hororok.monta.dto.response.item.PostItemResponseDto;
import com.hororok.monta.dto.response.shop.PurchaseResponseDtoV2;
import com.hororok.monta.entity.Item;
import com.hororok.monta.entity.ItemInventory;
import com.hororok.monta.entity.Member;
import com.hororok.monta.entity.TransactionRecord;
import com.hororok.monta.repository.ItemInventoryRepository;
import com.hororok.monta.repository.ItemRepository;
import com.hororok.monta.repository.MemberRepository;
import com.hororok.monta.repository.TransactionRecordRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@AllArgsConstructor
public class V2Service {

    private final ItemRepository itemRepository;
    private final ItemInventoryRepository itemInventoryRepository;
    private final TransactionRecordRepository transactionRecordRepository;
    private final MemberRepository memberRepository;
    private final MemberService memberService;

    @Transactional
    public ResponseEntity<?> postItem(PostItemRequestDto requestDto) {
        Item saveItem = itemRepository.save(new Item(requestDto));
        return ResponseEntity.status(HttpStatus.CREATED).body(new PostItemResponseDto(saveItem.getId()));
    }

    @Transactional
    public ResponseEntity<?> getItems() {
        List<Item> items = itemRepository.findAll();
        return ResponseEntity.status(HttpStatus.OK).body(new GetItemsResponseDto(items));
    }

    @Transactional
    public ResponseEntity<?> getItem(Long itemId) {
        Optional<Item> findItem = itemRepository.findOneById(itemId);

        if(findItem.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new FailResponseDto(HttpStatus.NOT_FOUND.name(), Collections.singletonList("존재하지 않는 아이템입니다.")));
        }
        Item item = findItem.get();
        return ResponseEntity.status(HttpStatus.OK).body(new GetItemResponseDto(item));
    }

    @Transactional
    public ResponseEntity<?> patchItem(PatchItemRequestDto requestDto, Long itemId) {

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
    public ResponseEntity<?> deleteItem(Long itemId) {

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

    @Transactional
    public ResponseEntity<?> postPurchase(PurchaseRequestDtoV2 requestDtoV2) {

        // item_id 존재하는지 체크
        long itemId = requestDtoV2.getItemId();
        Optional<Item> findItem = itemRepository.findOneById(itemId);

        if(findItem.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new FailResponseDto(HttpStatus.NOT_FOUND.name(), Collections.singletonList("존재하지 않는 Item 입니다.")));
        }
        Item item = findItem.get();

        // Member 정보 추출 (보유한 point, Food 갯수 파악 위함)
        Optional<Member> findMember = memberService.findMember(memberService.getMemberAccountId());

        if(findMember.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new FailResponseDto(HttpStatus.NOT_FOUND.name(), Collections.singletonList("존재하지 않는 유저 입니다.")));
        }
        Member member = findMember.get();

        // 수량 0 이상인지 체크
        if(requestDtoV2.getCount()<=0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new FailResponseDto(HttpStatus.BAD_REQUEST.name(), Collections.singletonList("수량을 1개 이상 선택해주세요.")));
        }

        // 구매할 포인트가 있는지 체크 (아이템*count < point)
        int purchaseCost = item.getCost() * requestDtoV2.getCount();

        if(purchaseCost > member.getPoint()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new FailResponseDto(HttpStatus.BAD_REQUEST.name(), Collections.singletonList("point가 부족합니다.")));
        }

        // item_type = "Food"
        if(Objects.equals(item.getItemType(), "Food")) {
            // 보유 갯수가 4개 미만인지 체크
            int existingFoodCount = itemInventoryRepository.countByMemberIdAndItemType(member.getId(), "Food");

            if (existingFoodCount + requestDtoV2.getCount() > 4) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new FailResponseDto(HttpStatus.BAD_REQUEST.name()
                                , Collections.singletonList("음식은 최대 4개까지 보유할 수 있습니다.")));
            }
        }

        // TransactionRecord 저장
        int point = member.getPoint() - purchaseCost;
        TransactionRecord transactionRecord = saveTransactionRecord(member, "Purchase", purchaseCost,
                requestDtoV2.getCount(), point, item.getName() + " " + requestDtoV2.getCount() + "개 구매");

        // ItemInventory 저장
        for(int i=0 ; i<requestDtoV2.getCount() ; i++) {
            int count=1;
            if(Objects.equals(item.getItemType(), "Consumable")) {
                count = 40;
            }
            itemInventoryRepository.save(new ItemInventory(item, member, count));
        }

        // Member point 저장
        member.updatePoint(point);
        memberRepository.save(member);

        // 성공 반환
        return ResponseEntity.status(HttpStatus.CREATED).body(new PurchaseResponseDtoV2(transactionRecord));
    }


    // 거래 내역 저장 메서드
    public TransactionRecord saveTransactionRecord(Member member, String type, int amount, int count, int point, String notes) {
        return transactionRecordRepository.save(new TransactionRecord(member, type, amount, count, point, notes));
    }

}
