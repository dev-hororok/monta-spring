package com.hororok.monta.repository;

import com.hororok.monta.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface MemberRepository extends JpaRepository<Member, UUID> {
    Member findOneByEmail(String email);
    boolean existsByEmail(String email);

}
