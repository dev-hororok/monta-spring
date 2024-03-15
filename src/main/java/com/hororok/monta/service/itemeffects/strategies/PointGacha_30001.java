package com.hororok.monta.service.itemeffects.strategies;

import com.hororok.monta.dto.response.itemInventory.UsePointBoxResponseDto;
import com.hororok.monta.entity.ItemInventory;
import com.hororok.monta.entity.Member;
import com.hororok.monta.repository.ItemInventoryRepository;
import com.hororok.monta.repository.MemberRepository;
import com.hororok.monta.service.itemeffects.EffectCode;
import com.hororok.monta.service.itemeffects.EffectCodeStrategy;
import com.hororok.monta.service.itemeffects.PointGacha;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

// Point 뽑기 B (100~500 당첨)
@EffectCode(30001)
@Component
public class PointGacha_30001 extends PointGacha implements EffectCodeStrategy {

    @Autowired
    public PointGacha_30001(MemberRepository memberRepository, ItemInventoryRepository itemInventoryRepository) {
        super(memberRepository, itemInventoryRepository);
    }

    @Override
    public ResponseEntity<?> useItem(ItemInventory itemInventory, Member member) {
        int point = randomPoint(100, 500);
        Member updateMember = updateMemberPoint(member, point);
        updateItemInventoryQuantity(itemInventory);
        return ResponseEntity.status(HttpStatus.OK).body(new UsePointBoxResponseDto(updateMember, point));
    }
}
