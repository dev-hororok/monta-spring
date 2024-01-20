package com.hororok.monta.service;

import com.hororok.monta.dto.request.member.PatchMemberRequestDto;
import com.hororok.monta.dto.response.*;
import com.hororok.monta.dto.response.member.*;
import com.hororok.monta.entity.Account;
import com.hororok.monta.entity.Member;
import com.hororok.monta.repository.AccountRepository;
import com.hororok.monta.repository.MemberRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class MemberService {

    private final MemberRepository memberRepository;
    private final AccountRepository accountRepository;

    public MemberService(MemberRepository memberRepository, AccountRepository accountRepository) {
        this.memberRepository = memberRepository;
        this.accountRepository = accountRepository;
    }

    @Transactional
    public ResponseEntity<?> getCurrentMember() {

        String email = getMemberEmail();

        if(!existMember(email)) {
            List<String> errors = new ArrayList<>();
            errors.add("가입이 필요합니다.");

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new FailResponseDto(HttpStatus.UNAUTHORIZED.value(), "정보 없음", errors));
        }

        Member member = memberRepository.findOneByEmail(email);
        return ResponseEntity.ok(new GetMemberResponseDto(new GetCurrentMemberDto(member)));
    }

    @Transactional
    public ResponseEntity<?> getMembers() {
        List<GetMembersDto> collectMember = memberRepository.findAll().stream().map(GetMembersDto::new).toList();
        return ResponseEntity.ok(new GetMembersResponseDto(collectMember));
    }

    @Transactional
    public ResponseEntity<?> getMember(UUID memberId) {

        if(!memberRepository.existsById(memberId)) {
            List<String> errors = new ArrayList<>();
            errors.add("존재하지 않는 회원입니다.");

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new FailResponseDto(HttpStatus.NOT_FOUND.value(), "정보 없음", errors));
        }

        Member member = memberRepository.findOneById(memberId);
        return ResponseEntity.ok(new GetMemberResponseDto(new GetMemberDto(member)));
    }

    @Transactional
    public ResponseEntity<?> postMember() {

        String email = getMemberEmail();
        Account accountMember = accountRepository.findOneByEmail(email);

        if(existMember(email)) {
            List<String> errors = new ArrayList<>();
            errors.add("이미 가입된 이메일 입니다.");

            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new FailResponseDto(HttpStatus.CONFLICT.value(), "생성 불가", errors));
        }

        // 랜덤 닉네임 생성 (UUID 6자리)
        String randomNickname = UUID.randomUUID().toString().substring(0,6);

        UUID saveMemberId = memberRepository.save(new Member(accountMember, randomNickname)).getId();
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new PostCreateMemberResponseDto(saveMemberId));

    }

    @Transactional
    public ResponseEntity<?> patchMember(PatchMemberRequestDto requestDto) {

        String email = getMemberEmail();

        if(!existMember(email)) {
            List<String> errors = new ArrayList<>();
            errors.add("사용자를 찾을 수 없습니다.");

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new FailResponseDto(HttpStatus.UNAUTHORIZED.value(), "수정 불가", errors));
        }

        Member member = memberRepository.findOneByEmail(email);

        String updateNickname = requestDto.getNickname();
        String updateImageUrl = requestDto.getImageUrl();

        if(requestDto.getNickname()==null) updateNickname = member.getNickname();
        if(requestDto.getImageUrl()==null) updateImageUrl = member.getImageUrl();

        member.updateMember(updateNickname, updateImageUrl);
        Member saveMember = memberRepository.save(member);

        return ResponseEntity.ok(new GetMemberResponseDto(new GetCurrentMemberDto(saveMember)));
    }

    @Transactional
    public ResponseEntity<?> deleteMember() {

        String email = getMemberEmail();

        if(!existMember(email)) {
            List<String> errors = new ArrayList<>();
            errors.add("사용자를 찾을 수 없습니다.");

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new FailResponseDto(HttpStatus.UNAUTHORIZED.value(), "삭제 불가", errors));
        }

        Member member = memberRepository.findOneByEmail(email);
        memberRepository.delete(member);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(new DeleteResponseDto());
    }


    public boolean existMember(String email) {
        return memberRepository.existsByEmail(email);
    }

    public String getMemberEmail() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }
}
