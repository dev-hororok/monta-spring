package com.hororok.monta.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@Entity
@Getter @Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@SQLDelete(sql = "UPDATE transaction_record SET deleted_at = CURRENT_TIMESTAMP WHERE transaction_record_id = ?")
@Where(clause = "deleted_at IS NULL")
public class TransactionRecord extends CommonEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "transaction_record_id")
    private long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @NotNull
    @Column(length=20)
    private String transactionType; // Purchase, Sell, Reward

    @NotNull
    private int amount;

    @NotNull
    private int count;

    @NotNull
    private int balanceAfterTransaction;

    @NotNull
    private String notes;

    public TransactionRecord(Member member, String type, int amount, int count, int point, String notes) {
        this.member = member;
        this.transactionType = type;
        this.amount = amount;
        this.count = count;
        this.balanceAfterTransaction = point;
        this.notes = notes;
    }

}
