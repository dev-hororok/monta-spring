package com.hororok.monta.service;

import com.hororok.monta.dto.request.transaction.RewardRequestDto;
import com.hororok.monta.dto.response.FailResponseDto;
import com.hororok.monta.dto.response.transaction.RewardResponseDto;
import com.hororok.monta.entity.Member;
import com.hororok.monta.entity.TransactionRecord;
import com.hororok.monta.repository.MemberRepository;
import com.hororok.monta.repository.TransactionRecordRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Optional;

@Service
@AllArgsConstructor
public class TransactionService {
    private final MemberRepository memberRepository;
    private final TransactionRecordRepository transactionRecordRepository;
    private final MemberService memberService;

    @Transactional
    public ResponseEntity<?> addReward(RewardRequestDto requestDto) {
        // Member 정보 추출
        Optional<Member> findMember = memberService.findMemberDetails(memberService.findMemberAccountId());

        if(findMember.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new FailResponseDto(HttpStatus.NOT_FOUND.name(), Collections.singletonList("존재하지 않는 유저 입니다.")));
        }
        Member member = findMember.get();

        // Member Point Update
        int point = member.getPoint() + requestDto.getPoint();
        member.updatePoint(point);
        memberRepository.save(member);

        // Transaction 기록
        transactionRecordRepository.save(new TransactionRecord(member, "Reward", requestDto.getPoint(), 1, point, "Reward point 획득 : " + requestDto.getPoint()));

        // 성공 반환
        return ResponseEntity.status(HttpStatus.OK).body(new RewardResponseDto(requestDto.getPoint(), point));
    }
}
