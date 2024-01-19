package com.hororok.monta.repository;

import com.hororok.monta.entity.GameCharacter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CharacterRepository extends JpaRepository<GameCharacter, UUID> {
    boolean existsByName(String Name);

    List<GameCharacter> findAll();

    List<GameCharacter> findByGrade(String grade);
}
