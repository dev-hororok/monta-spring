package com.hororok.monta.repository;

import com.hororok.monta.entity.EggInventory;
import com.hororok.monta.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface EggInventoryRepository extends JpaRepository<EggInventory, UUID> {
    int countByMember(Member member);
}
