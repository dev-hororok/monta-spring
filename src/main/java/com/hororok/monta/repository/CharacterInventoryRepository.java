package com.hororok.monta.repository;

import com.hororok.monta.entity.CharacterInventory;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.UUID;

public interface CharacterInventoryRepository extends JpaRepository<CharacterInventory, Long> {
    Optional<CharacterInventory> findOneByMemberIdAndCharacterId(UUID memberId, int characterId);

    Optional<CharacterInventory> findOneByMemberId(UUID memberId);
}
