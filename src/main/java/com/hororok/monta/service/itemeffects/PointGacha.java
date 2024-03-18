package com.hororok.monta.service.itemeffects;

import com.hororok.monta.entity.ItemInventory;
import com.hororok.monta.entity.Member;
import com.hororok.monta.entity.TransactionRecord;
import com.hororok.monta.repository.ItemInventoryRepository;
import com.hororok.monta.repository.MemberRepository;
import com.hororok.monta.repository.TransactionRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PointGacha {
    protected MemberRepository memberRepository;
    protected ItemInventoryRepository itemInventoryRepository;
    protected TransactionRecordRepository transactionRecordRepository;

    @Autowired
    public PointGacha(MemberRepository memberRepository, ItemInventoryRepository itemInventoryRepository,
                      TransactionRecordRepository transactionRecordRepository) {
        this.memberRepository = memberRepository;
        this.itemInventoryRepository = itemInventoryRepository;
        this.transactionRecordRepository = transactionRecordRepository;
    }

    protected int randomPoint(int startPoint, int endPoint) {
        return startPoint + (int)(Math.random() * (endPoint - startPoint + 1));
    }

    protected Member updateMemberPoint(Member member, int point) {
        member.updatePoint(member.getPoint() + point);
        return memberRepository.save(member);
    }

    protected void deductItemInventoryQuantity(ItemInventory itemInventory) {
        itemInventory.updateQuantity(itemInventory.getQuantity() - 1);
        itemInventoryRepository.save(itemInventory);
    }

    public void recordTransaction(Member member, int point) {
        transactionRecordRepository.save(new TransactionRecord(member, "Acquisition", 0,
                1, member.getPoint(), "Point 획득 : " + point));
    }

    protected void deleteItemInventory(ItemInventory itemInventory) {
        itemInventoryRepository.save(itemInventory);
        itemInventoryRepository.delete(itemInventory);
    }
}

