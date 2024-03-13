package com.hororok.monta.service.useItem;

import com.hororok.monta.entity.ItemInventory;
import com.hororok.monta.entity.Member;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

public interface ItemUseStrategy {
    ResponseEntity<?> useItem(ItemInventory itemInventory, Member member);
}
