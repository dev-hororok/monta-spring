package com.hororok.monta.utils;

import com.hororok.monta.entity.Member;
import com.hororok.monta.repository.MemberTestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
public class MemberUtils {
    @Autowired
    private MemberTestRepository memberTestRepository;

    public void setPoint() {
        memberTestRepository.setPoint();
    }

    public Member findMember(UUID memberId) {
        Optional<Member> findMember = memberTestRepository.findById(memberId);
        return findMember.get();
    }
}
