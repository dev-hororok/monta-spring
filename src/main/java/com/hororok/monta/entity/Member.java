package com.hororok.monta.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Member extends CommonEntity{

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @JdbcTypeCode(SqlTypes.VARCHAR)
    @Column(name = "member_id")
    private UUID id;

    @NotNull
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id")
    private Account account;

    @NotNull
    @Column(length=100)
    private String nickname;

    @NotNull @Email
    @Column(length=100)
    private String email;

    private String imageUrl;

    @NotNull
    @Enumerated(EnumType.STRING)
    private Authority role;      // ADMIN, USER

    @JdbcTypeCode(SqlTypes.VARCHAR)
    private UUID activeEggId;

    private Long activeRecordId;

    @NotNull
    private int point;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TransactionRecord> transactionRecords = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<EggInventory> eggInventories = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CharacterInventory> characterInventories = new ArrayList<>();

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "study_streak_id")
    private StudyStreak studyStreak;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "statistic_id")
    private Statistic statistic;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<StudyCategory> studyCategories = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<StudyRecord> studyRecords = new ArrayList<>();

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "streak_color_change_permission_id")
    private StreakColorChangePermission streakColorChangePermission;


    public Member(Account account, String randomNickname) {
        this.account = account;
        this.nickname = randomNickname;
        this.email = account.getEmail();
        this.role = account.getRole();
        this.point = 0;
    }

    public void updateMember(String nickname, String imageUrl) {
        this.nickname = nickname;
        this.imageUrl = imageUrl;
    }
}
