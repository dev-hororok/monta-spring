package com.hororok.monta.repository;

import com.hororok.monta.entity.EggInventory;
import com.hororok.monta.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface EggInventoryRepository extends JpaRepository<EggInventory, UUID> {
    int countByMember(Member member);
}
