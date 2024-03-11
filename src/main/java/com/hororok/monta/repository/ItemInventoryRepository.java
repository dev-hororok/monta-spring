package com.hororok.monta.repository;

import com.hororok.monta.entity.ItemInventory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ItemInventoryRepository extends JpaRepository<ItemInventory, Long> {

    int countByMemberIdAndItemType(UUID memberId, String itemType);

    Optional<ItemInventory> findByIdAndMemberId(long itemInventoryId, UUID memberId);

    Optional<ItemInventory> findByMemberIdAndItemType(UUID memberId, String itemType);

    List<ItemInventory> findAllByMemberIdAndItemId(UUID memberId, int ItemId);
}
