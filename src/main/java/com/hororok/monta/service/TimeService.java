package com.hororok.monta.service;

import com.hororok.monta.dto.request.time.ReduceTimeRequestDto;
import com.hororok.monta.dto.response.FailResponseDto;
import com.hororok.monta.dto.response.time.ReduceTimeResponseDto;
import com.hororok.monta.entity.ItemInventory;
import com.hororok.monta.entity.Member;
import com.hororok.monta.repository.ItemInventoryRepository;
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
public class TimeService {
    private final MemberService memberService;
    private final ItemInventoryRepository itemInventoryRepository;

    @Transactional
    public ResponseEntity<?> reduceTime(ReduceTimeRequestDto requestDto) {
        // Member 정보 추출
        Optional<Member> findMember = memberService.findMemberDetails(memberService.findMemberAccountId());
        if(findMember.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new FailResponseDto(HttpStatus.NOT_FOUND.name(), Collections.singletonList("존재하지 않는 유저 입니다.")));
        }
        Member member = findMember.get();

        // item_inventory List 추출 -> Progress 줄여 주기
        List<ItemInventory> itemInventoryList = itemInventoryRepository.findByMemberIdAndProgressAndQuantity(member.getId());
        for(ItemInventory itemInventory : itemInventoryList) {
            if(itemInventory.getProgress() < requestDto.getSeconds()) {
                itemInventory.updateProgress(0);
            } else {
                itemInventory.updateProgress(itemInventory.getProgress() - requestDto.getSeconds());
            }
            itemInventoryRepository.save(itemInventory);
        }
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(new ReduceTimeResponseDto());
    }
}
