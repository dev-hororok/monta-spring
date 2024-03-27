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

    ItemInventory findOneByMemberIdAndItemType(UUID memberId, String itemType);

    ItemInventory findByMemberIdAndItemId(UUID memberId, int itemId);

    @Transactional
    @Modifying
    @Query(value = "DELETE FROM item_inventory WHERE item_inventory_id = :id", nativeQuery = true)
    void deleteTestData(@Param("id") Long itemInventoryId);
}
