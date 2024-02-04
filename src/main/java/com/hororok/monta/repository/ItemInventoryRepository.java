package com.hororok.monta.repository;

import com.hororok.monta.entity.ItemInventory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ItemInventoryRepository extends JpaRepository<ItemInventory, Long> {

    int countByMemberIdAndItemType(UUID memberId, String itemType);
}
