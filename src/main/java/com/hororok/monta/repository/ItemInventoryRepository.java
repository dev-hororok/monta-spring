package com.hororok.monta.repository;

import com.hororok.monta.entity.ItemInventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ItemInventoryRepository extends JpaRepository<ItemInventory, Long> {
    int countByMemberIdAndItemType(UUID memberId, String itemType);

    Optional<ItemInventory> findByIdAndMemberId(long itemInventoryId, UUID memberId);

    Optional<ItemInventory> findByItemIdAndMemberId(int itemId, UUID memberId);

    @Query("SELECT ii FROM ItemInventory ii WHERE ii.member.id = :memberId AND ii.progress > 0 AND ii.quantity > 0")
    List<ItemInventory> findByMemberIdAndProgressAndQuantity(UUID memberId);
}
