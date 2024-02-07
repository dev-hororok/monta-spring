package com.hororok.monta.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
public class StudyCategory {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="study_category_id")
    private long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @NotNull
    @Column(length = 50)
    private String subject;

    @NotNull
    @Column(length = 7)
    private String color;

    @OneToMany(mappedBy = "studyCategory")
    private List<StudyRecord> studyRecords = new ArrayList<>();

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

}