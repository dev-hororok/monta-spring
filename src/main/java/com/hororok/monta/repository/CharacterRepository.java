package com.hororok.monta.repository;

import com.hororok.monta.entity.Character;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface CharacterRepository extends JpaRepository<Character, Integer> {
    boolean existsByName(String Name);

    List<Character> findAll();

    List<Character> findByGrade(String grade);
}
