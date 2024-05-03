package com.hororok.monta.service.itemeffects.strategies;

import com.hororok.monta.dto.response.itemInventory.UsePointGachaResponseDto;
import com.hororok.monta.entity.ItemInventory;
import com.hororok.monta.entity.Member;
import com.hororok.monta.repository.ItemInventoryRepository;
import com.hororok.monta.repository.MemberRepository;
import com.hororok.monta.repository.TransactionRecordRepository;
import com.hororok.monta.service.itemeffects.EffectCode;
import com.hororok.monta.service.itemeffects.EffectCodeStrategy;
import com.hororok.monta.service.itemeffects.PointGacha;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

// Point 뽑기 (동 주머니 : 100~500 당첨)
@EffectCode(30001)
@Component
public class PointGacha_30001 extends PointGacha implements EffectCodeStrategy {
    @Autowired
    public PointGacha_30001(MemberRepository memberRepository, ItemInventoryRepository itemInventoryRepository,
                            TransactionRecordRepository transactionRecordRepository) {
        super(memberRepository, itemInventoryRepository, transactionRecordRepository);
    }

    @Override
    public ResponseEntity<?> useItem(ItemInventory itemInventory, Member member) {
        // 랜덤 포인트 추출
        int point = randomPoint(100, 500);

        // 멤버의 point update
        Member updateMember = updateMemberPoint(member, point);

        // 사용한 아이템 수량 차감
        deductItemInventoryQuantity(itemInventory);

        // Transaction 기록
        recordTransaction(member, point);

        return ResponseEntity.status(HttpStatus.OK).body(new UsePointGachaResponseDto(updateMember, point));
    }
}
