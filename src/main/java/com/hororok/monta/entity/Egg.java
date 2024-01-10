package com.hororok.monta.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
public class Egg extends CommonEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "egg_id", columnDefinition = "BINARY(16)")
    private UUID id;

    @NotBlank
    @Column(length = 100)
    private String name;

    @NotBlank
    private String description;

    @NotBlank
    private int requiredStudyTime; // 부화시키기 위해 필요한 공부시간을 second 단위로 시간 측정

    @NotBlank
    private int purchasePrice;

    @NotBlank
    private String imageUrl;

    @NotBlank
    @Column(length = 10)
    private String grade; // SS, S+, S, A, B, C, AD

    @OneToMany(mappedBy = "egg", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<EggInventory> eggInventories = new ArrayList<>();
}
