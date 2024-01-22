package com.hororok.monta.repository;

import com.hororok.monta.entity.CharacterInventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CharacterInventoryRepository extends JpaRepository<CharacterInventory, Long> {
    List<CharacterInventory> findByMemberIdAndCharacterId(UUID memberId, UUID characterId);
}