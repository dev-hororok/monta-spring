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
        Optional<Item> findItem = itemRepository.findOneByItemId(itemId);

        if(findItem.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new FailResponseDto(HttpStatus.NOT_FOUND.name(), Collections.singletonList("존재하지 않는 아이템입니다.")));
        }
        Item item = findItem.get();
        return ResponseEntity.status(HttpStatus.OK).body(new GetItemResponseDto(item));
    }

    @Transactional
    public ResponseEntity<?> patchItem(PatchItemRequestDto requestDto, Long itemId) {

        Optional<Item> findItem = itemRepository.findOneByItemId(itemId);

        // 아이템 존재 여부 점검
        if(findItem.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new FailResponseDto(HttpStatus.NOT_FOUND.name(), Collections.singletonList("존재하지 않는 아이템입니다.")));
        }

        // 공백 필드 기존 값으로 채워주기
        Item item = findItem.get();
        if(requestDto.getItemType().isEmpty()) requestDto.setItemType(item.getItemType());
        if(requestDto.getName().isEmpty()) requestDto.setName(item.getName());
        if(requestDto.getGrade().isEmpty()) requestDto.setGrade(item.getGrade());
        if(requestDto.getDescription().isEmpty()) requestDto.setDescription(item.getDescription());
        if(requestDto.getImageUrl().isEmpty()) requestDto.setImageUrl(item.getImageUrl());
        if(requestDto.getCost()==null) requestDto.setCost(item.getCost());
        if(requestDto.getRequiredStudyTime()==null) requestDto.setRequiredStudyTime(item.getRequiredStudyTime());
        if(requestDto.getEffectCode()==null) requestDto.setEffectCode(item.getEffectCode());
        if(requestDto.isHidden()) requestDto.setHidden(item.isHidden());

        // DB 수정
        Item updateItem = itemRepository.save(new Item(requestDto));
        return ResponseEntity.status(HttpStatus.OK).body(new PatchItemResponseDto(updateItem));
    }

    @Transactional
    public ResponseEntity<?> deleteItem(Long itemId) {

        Optional<Item> findItem = itemRepository.findOneByItemId(itemId);

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
