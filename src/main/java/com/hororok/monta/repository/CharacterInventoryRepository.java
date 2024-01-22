package com.hororok.monta.repository;

import com.hororok.monta.entity.CharacterInventory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface CharacterInventoryRepository extends JpaRepository<CharacterInventory, Long> {
    List<CharacterInventory> findByMemberIdAndCharacterId(UUID memberId, UUID characterId);
}