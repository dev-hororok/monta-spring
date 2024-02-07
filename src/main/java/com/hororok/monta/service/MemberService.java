package com.hororok.monta.service;

import com.hororok.monta.dto.response.member.*;
import com.hororok.monta.entity.*;
import com.hororok.monta.repository.*;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@AllArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    @Transactional
    public ResponseEntity<?> getMembers() {
        List<Member> collectMember = memberRepository.findAll();
        return ResponseEntity.ok(new GetMembersResponseDto(collectMember));
    }

    public Optional<Member> findMember(UUID accountId) {
        return memberRepository.findOneByAccountId(accountId);
    }

    public UUID getMemberAccountId() {
        return UUID.fromString(SecurityContextHolder.getContext().getAuthentication().getName());
    }
}
