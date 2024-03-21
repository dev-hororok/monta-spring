package com.hororok.monta.repository;

import com.hororok.monta.entity.CharacterInventory;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface CharacterInventoryTestRepository extends CrudRepository<CharacterInventory, Integer> {
    @Transactional
    @Modifying
    @Query(value = "UPDATE character_inventory SET quantity = 5 WHERE character_inventory_id = 1", nativeQuery = true)
    void setQuantity();

    @Transactional
    @Modifying
    @Query(value = "DELETE FROM character_inventory WHERE character_inventory_id = :id", nativeQuery = true)
    void deleteTestData(@Param("id") long characterInventoryId);
}
