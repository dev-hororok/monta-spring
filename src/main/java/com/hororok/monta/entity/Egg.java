package com.hororok.monta.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
public class Egg extends CommonEntity{
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @JdbcTypeCode(SqlTypes.VARCHAR)
    @Column(name = "egg_id")
    private UUID id;

    @NotNull
    @Column(length = 100)
    private String name;

    @NotNull
    private String description;

    @NotNull
    private int requiredStudyTime; // 부화시키기 위해 필요한 공부시간을 second 단위로 시간 측정

    @NotNull
    private int purchasePrice;

    @NotNull
    private String imageUrl;

    @NotNull
    @Column(length = 10)
    private String grade; // SS, S+, S, A, B, C, AD

    @OneToMany(mappedBy = "egg", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<EggInventory> eggInventories = new ArrayList<>();
}
