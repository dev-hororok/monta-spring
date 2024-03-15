package com.hororok.monta.service.itemeffects;

import com.hororok.monta.entity.ItemInventory;
import com.hororok.monta.entity.Member;
import com.hororok.monta.repository.ItemInventoryRepository;
import com.hororok.monta.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PointGacha {
    protected MemberRepository memberRepository;
    protected ItemInventoryRepository itemInventoryRepository;

    @Autowired
    public PointGacha(MemberRepository memberRepository, ItemInventoryRepository itemInventoryRepository) {
        this.memberRepository = memberRepository;
        this.itemInventoryRepository = itemInventoryRepository;
    }

    protected int randomPoint(int startPoint, int endPoint) {
        return startPoint + (int)(Math.random() * (endPoint - startPoint + 1));
    }

    protected Member updateMemberPoint(Member member, int point) {
        member.updatePoint(member.getPoint() + point);
        return memberRepository.save(member);
    }

    protected void updateItemInventoryQuantity(ItemInventory itemInventory) {
        itemInventory.updateQuantity(itemInventory.getQuantity()-1);
        itemInventoryRepository.save(itemInventory);
    }
}

