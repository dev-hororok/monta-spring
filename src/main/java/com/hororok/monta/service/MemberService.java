package com.hororok.monta.service;

import com.hororok.monta.entity.Account;
import com.hororok.monta.entity.Authority;
import com.hororok.monta.entity.Member;
import com.hororok.monta.exception.DuplicateMemberException;
import com.hororok.monta.repository.MemberRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public MemberService(MemberRepository memberRepository, PasswordEncoder passwordEncoder) {
        this.memberRepository = memberRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public MemberDto signup(MemberDto memberDto) {
        if (memberRepository.findOneByEmail(memberDto.) {
            throw new DuplicateMemberException("이미 가입되어 있는 유저입니다.");
        }

        Account account = Account.builder().role(Authority.valueOf("USER")).build();

        // 여기 너무 간추려짐?
        Member member = Member.builder()
                .nickname(memberDto.getNickname())
                .build();

        return MemberDto.from(memberRepository.save(member));
    }

}
