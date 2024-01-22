package com.hororok.monta.repository;

import com.hororok.monta.entity.Character;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CharacterRepository extends JpaRepository<Character, UUID> {
    boolean existsByName(String Name);

    List<Character> findAll();

    List<Character> findByGrade(String grade);
}
