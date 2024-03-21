package com.hororok.monta.repository;

import com.hororok.monta.entity.ItemInventory;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

public interface ItemInventoryTestRepository extends CrudRepository<ItemInventory, Long> {
    List<ItemInventory> findAllByMemberIdAndItemType(UUID memberId, String itemType);

    ItemInventory findByMemberIdAndItemType(UUID memberId, String itemType);

    @Transactional
    @Modifying
    @Query("DELETE FROM ItemInventory i WHERE i.id = :id")
    void deleteTestData(@Param("id") Long itemInventoryId);
}
