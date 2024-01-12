package com.hororok.monta.repository;

import com.hororok.monta.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, String> {
    Optional<Member> findOneByEmail(String email);
}
