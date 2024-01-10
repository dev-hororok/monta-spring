package com.hororok.monta.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
public class Member extends CommonEntity{

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id", columnDefinition = "BINARY(16)")
    private UUID id;

    @NotBlank
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id")
    private Account account;

    @NotBlank
    @Column(length=100)
    private String nickname;

    @NotBlank @Email
    @Column(length=100)
    private String email;

    private String imageUrl;

    @NotBlank
    @Enumerated(EnumType.STRING)
    private Authority role;      // ADMIN, USER

    private long activeRecordId;

    @Column(columnDefinition = "BINARY(16)")
    private UUID activeEggId;

    @NotBlank
    private int point;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TransactionRecord> transactionRecords = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<EggInventory> eggInventories = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CharacterInventory> characterInventories = new ArrayList<>();

    @OneToOne(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private StudyStreak studyStreaks;

    @OneToOne(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private Statistic statistic;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<StudyCategory> studyCategories = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<StudyRecord> studyRecords = new ArrayList<>();

    @OneToOne(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private StreakColorChangePermission streakColorChangePermission;

}
