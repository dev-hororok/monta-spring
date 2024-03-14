package com.hororok.monta.service.itemeffects;

import com.hororok.monta.entity.ItemInventory;
import com.hororok.monta.entity.Member;
import org.springframework.http.ResponseEntity;

public interface EffectCodeStrategy {
    ResponseEntity<?> useItem(ItemInventory itemInventory, Member member);
}
