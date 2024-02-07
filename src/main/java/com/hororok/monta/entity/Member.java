package com.hororok.monta.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
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
@SQLDelete(sql = "UPDATE member SET deleted_at = CURRENT_TIMESTAMP WHERE member_id = ?")
@Where(clause = "deleted_at IS NULL")
public class Member extends CommonEntity{

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @JdbcTypeCode(SqlTypes.VARCHAR)
    @Column(name = "member_id")
    private UUID id;

    @NotNull
    @JdbcTypeCode(SqlTypes.VARCHAR)
    private UUID accountId;

    @NotNull @Email
    @Column(length=100)
    private String email;

    @NotNull
    @Column(length=100)
    private String nickname;

    private String imageUrl;

    @NotNull
    @Column(length=20)
    private String role;      // Admin, User

    @NotNull
    private int point;

    private Long activeRecordId;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<TransactionRecord> transactionRecords = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<CharacterInventory> characterInventories = new ArrayList<>();

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "study_streak_id")
    private StudyStreak studyStreak;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<StudyCategory> studyCategories = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<StudyRecord> studyRecords = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<ItemInventory> itemInventories = new ArrayList<>();


    public void updateMember(String nickname, String imageUrl) {
        this.nickname = nickname;
        this.imageUrl = imageUrl;
    }

    public void updateActiveRecordId(Long activeRecordId) {
        this.activeRecordId = activeRecordId;
    }

    public void updatePoint(int point) {
        this.point = point;
    }

}
