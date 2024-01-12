package com.hororok.monta.repository;

import com.hororok.monta.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AccountRepository extends JpaRepository<Account, UUID> {

    boolean existsByEmail(String email);

}
