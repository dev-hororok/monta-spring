package com.hororok.monta.repository;

import com.hororok.monta.entity.Item;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ItemTestRepository extends CrudRepository<Item, Integer> {

    @Transactional
    @Modifying
    @Query("DELETE FROM Item i WHERE i.id = :id")
    void deleteTestData(@Param("id") Integer itemId);

    @Transactional
    @Modifying
    @Query(value = "UPDATE item SET deleted_at = null WHERE item_id = :id", nativeQuery = true)
    void setDeletedAtNullById(@Param("id") Integer itemId);

    List<Item> findAllByItemType(String itemType);
}
