package com.hororok.monta.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class ItemInventory extends CommonEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "item_inventory_id")
    private long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @NotNull
    @Column(length = 20)
    private String itemType;

    private Integer progress;

    @NotNull
    private int quantity;

    public ItemInventory(Item item, Member member, int quantity) {
        this.item = item;
        this.member = member;
        this.itemType = item.getItemType();
        this.progress = item.getRequiredStudyTime();
        this.quantity = quantity;
    }

}
