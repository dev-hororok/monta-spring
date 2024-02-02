package com.hororok.monta.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
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
}
