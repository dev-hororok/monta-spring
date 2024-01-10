package com.hororok.monta.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.time.LocalDateTime;

@Entity
@Getter
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "transaction_id")
    private long id;

    @NotBlank
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @NotBlank
    private LocalDateTime transactionDate;

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
    @Column(length = 255)
    private String notes;
}
