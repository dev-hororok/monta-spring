package com.hororok.monta.repository;

import com.hororok.monta.entity.Account;
import com.hororok.monta.entity.Member;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface AccountRepository extends JpaRepository<Account, UUID> {

    boolean existsByEmail(String email);

    @EntityGraph(attributePaths = "role")
    Optional<Account> findOneWithRoleByEmail(String email);

    Account findOneByEmail(String email);
}
