package com.hororok.monta.service;

import com.hororok.monta.dto.request.item.PatchItemRequestDto;
import com.hororok.monta.dto.request.item.PostItemRequestDto;
import com.hororok.monta.dto.response.DeleteResponseDto;
import com.hororok.monta.dto.response.FailResponseDto;
import com.hororok.monta.dto.response.item.GetItemResponseDto;
import com.hororok.monta.dto.response.item.GetItemsResponseDto;
import com.hororok.monta.dto.response.item.PatchItemResponseDto;
import com.hororok.monta.dto.response.item.PostItemResponseDto;
import com.hororok.monta.entity.Item;
import com.hororok.monta.repository.ItemRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class V2Service {

    private final ItemRepository itemRepository;

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

}
