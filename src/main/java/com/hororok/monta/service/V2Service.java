package com.hororok.monta.service;

import com.hororok.monta.dto.request.item.PostItemRequestDto;
import com.hororok.monta.dto.response.item.PostItemResponseDto;
import com.hororok.monta.entity.Item;
import com.hororok.monta.repository.ItemRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class V2Service {

    private final ItemRepository itemRepository;

    @Transactional
    public ResponseEntity<?> postItem(PostItemRequestDto requestDto) {
        Item saveItem = itemRepository.save(new Item(requestDto));
        return ResponseEntity.status(HttpStatus.CREATED).body(new PostItemResponseDto(saveItem.getId()));
    }

}
