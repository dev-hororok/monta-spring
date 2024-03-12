package com.hororok.monta.repository;

import com.hororok.monta.entity.Character;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface CharacterTestRepository extends CrudRepository<Character, Integer> {



    @Transactional
    @Modifying
    @Query("DELETE FROM Character c WHERE c.id = :id")
    void deleteTestData(@Param("id") Integer characterId);

    @Transactional
    @Modifying
    @Query(value = "UPDATE `character` SET deleted_at = null WHERE character_id = :id", nativeQuery = true)
    void setDeletedAtNullById(@Param("id") Integer characterId);
}
