package com.hororok.monta.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Entity
@Getter
public class TransactionRecord extends CommonEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "transaction_record_id")
    private long id;

    @NotBlank
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @NotBlank
    @Enumerated(EnumType.STRING)
    private TransactionType transactionType; // PURCHASE, SELL, REWARD

    @NotBlank
    private int amount;

    @NotBlank
    private int count;

    @NotBlank
    private int balanceAfterTransaction;

    @NotBlank
    private String notes;
}
