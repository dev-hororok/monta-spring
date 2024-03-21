package com.hororok.monta.repository;

import com.hororok.monta.entity.Item;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface ItemTestRepository extends CrudRepository<Item, Integer> {
    @Transactional
    @Modifying
    @Query(value = "DELETE FROM item WHERE item_id = :id", nativeQuery = true)
    void deleteTestData(@Param("id") Integer itemId);

}
