package com.hororok.monta.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Entity
@Getter
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
    @Enumerated(EnumType.STRING)
    private TransactionType transactionType; // PURCHASE, SELL, REWARD

    @NotNull
    private int amount;

    @NotNull
    private int count;

    @NotNull
    private int balanceAfterTransaction;

    @NotNull
    private String notes;
}
