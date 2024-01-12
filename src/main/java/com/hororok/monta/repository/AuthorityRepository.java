package com.hororok.monta.repository;

import com.hororok.monta.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorityRepository extends JpaRepository<Account, String> {
}
