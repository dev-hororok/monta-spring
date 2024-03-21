package com.hororok.monta.service;

import com.hororok.monta.dto.request.item.UpdateItemRequestDto;
import com.hororok.monta.dto.request.item.CreateItemRequestDto;
import com.hororok.monta.dto.response.DeleteResponseDto;
import com.hororok.monta.dto.response.FailResponseDto;
import com.hororok.monta.dto.response.item.GetItemResponseDto;
import com.hororok.monta.dto.response.item.GetItemsResponseDto;
import com.hororok.monta.dto.response.item.UpdateItemResponseDto;
import com.hororok.monta.dto.response.item.CreateItemResponseDto;
import com.hororok.monta.entity.*;
import com.hororok.monta.repository.*;
import com.hororok.monta.service.itemeffects.EffectCodeStrategy;
import com.hororok.monta.service.itemeffects.EffectCodeStrategyFactory;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ItemService {
    private final ItemRepository itemRepository;
    private final ItemInventoryRepository itemInventoryRepository;
    private final MemberService memberService;
    private final EffectCodeStrategyFactory effectCodeStrategyFactory;

    @Transactional
    public ResponseEntity<?> addItemDetails(CreateItemRequestDto requestDto) {
        Item saveItem = itemRepository.save(new Item(requestDto));
        return ResponseEntity.status(HttpStatus.CREATED).body(new CreateItemResponseDto(saveItem.getId()));
    }

    @Transactional
    public ResponseEntity<?> findItemList() {
        List<Item> items = itemRepository.findAll();
        return ResponseEntity.status(HttpStatus.OK).body(new GetItemsResponseDto(items));
    }

    @Transactional
    public ResponseEntity<?> findItemDetails(int itemId) {
        Optional<Item> findItem = itemRepository.findOneById(itemId);

        if(findItem.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new FailResponseDto(HttpStatus.NOT_FOUND.name(), Collections.singletonList("존재하지 않는 아이템입니다.")));
        }
        Item item = findItem.get();
        return ResponseEntity.status(HttpStatus.OK).body(new GetItemResponseDto(item));
    }

    @Transactional
    public ResponseEntity<?> updateItemDetails(UpdateItemRequestDto requestDto, int itemId) {

        Optional<Item> findItem = itemRepository.findOneById(itemId);

        // 아이템 존재 여부 점검
        if(findItem.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new FailResponseDto(HttpStatus.NOT_FOUND.name(), Collections.singletonList("존재하지 않는 아이템입니다.")));
        }

        // 빈 값이 아니면 새로운 값으로 수정
        Item item = findItem.get();

        List<UpdateItemRequestDto> list = new ArrayList<>();
        list.add(requestDto);

        String itemType = item.getItemType();
        String name = item.getName();
        String grade = item.getGrade();
        String description = item.getDescription();
        String imageUrl = item.getImageUrl();
        int cost = item.getCost();
        Integer requiredStudyTime = item.getRequiredStudyTime();
        int effectCode = item.getEffectCode();
        Boolean isHidden = item.getIsHidden();

        for (UpdateItemRequestDto itemRequestDto : list) {
            if (itemRequestDto.getItemType() != null) itemType = itemRequestDto.getItemType();
            if (itemRequestDto.getName() != null) name = itemRequestDto.getName();
            if (itemRequestDto.getGrade() != null) grade = itemRequestDto.getGrade();
            if (itemRequestDto.getDescription() != null) description = itemRequestDto.getDescription();
            if (itemRequestDto.getImageUrl() != null) imageUrl = itemRequestDto.getImageUrl();
            if (itemRequestDto.getCost() != null) cost = itemRequestDto.getCost();
            if (itemRequestDto.getRequiredStudyTime() != null) requiredStudyTime = itemRequestDto.getRequiredStudyTime();
            if (itemRequestDto.getEffectCode() != null) effectCode = itemRequestDto.getEffectCode();
            if (itemRequestDto.getIsHidden() != null) isHidden = itemRequestDto.getIsHidden();
        }

        // DB 수정
        item.updateItem(itemType, name, grade, description, imageUrl, cost, requiredStudyTime, effectCode, isHidden);
        Item updateItem = itemRepository.save(item);
        return ResponseEntity.status(HttpStatus.OK).body(new UpdateItemResponseDto(updateItem));
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
        Optional<Member> findMember = memberService.findMemberDetails(memberService.findMemberAccountId());
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
        if(itemInventory.getQuantity() <= 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new FailResponseDto(HttpStatus.BAD_REQUEST.name(), Collections.singletonList("사용할 수 있는 수량이 없습니다.")));
        }

        EffectCodeStrategy strategy = effectCodeStrategyFactory.getStrategy(itemInventory.getItem());

        // 이외의 이상이 발생하는 경우
        if(strategy==null) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new FailResponseDto(HttpStatus.INTERNAL_SERVER_ERROR.name()
                            , Collections.singletonList("서버 오류 : 아이템 효과 없음 (운영자에게 문의해주세요)")));
        }

        return strategy.useItem(itemInventory, member);
    }
}
