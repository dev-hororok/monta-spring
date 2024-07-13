package com.hororok.monta.repository;

import com.hororok.monta.entity.MemberCharacterCollection;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface MemberCharacterCollectionRepository extends JpaRepository<MemberCharacterCollection, Integer> {
    Optional<MemberCharacterCollection> findByMemberIdAndCharacterId(UUID memberId, int characterId);
}
