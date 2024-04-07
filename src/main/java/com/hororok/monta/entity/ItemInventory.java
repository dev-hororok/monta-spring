package com.hororok.monta.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@SQLDelete(sql = "UPDATE item_inventory SET deleted_at = CURRENT_TIMESTAMP WHERE item_inventory_id = ?")
@Where(clause = "deleted_at IS NULL")
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

    public void updateQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void updateProgress(int progress) {
        this.progress = progress;
    }
}
