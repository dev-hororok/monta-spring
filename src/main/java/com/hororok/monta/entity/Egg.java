package com.hororok.monta.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

import java.util.UUID;

@Entity
@Getter
public class Egg {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private UUID id;

    @NotBlank
    @Column(length = 50)
    private String name;

    @NotBlank
    @Column(length = 255)
    private String description;

    @NotBlank
    private int requiredStudyTime; // 부화시키기 위해 필요한 공부시간을 second 단위로 시간 측정

    @NotBlank
    private int purchasePrice;

    @NotBlank
    @Column(length = 255)
    private String imageUrl;

    @NotBlank
    @Column(length = 10)
    private String grade; // SS, S+, S, A, B, C, AD
}
