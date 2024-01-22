package com.hororok.monta.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Entity
@Getter
@NoArgsConstructor
public class StudyRecord extends CommonEntity{

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "study_record_id")
    private long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "study_category_id")
    private StudyCategory studyCategory;

    private int duration;

    public StudyRecord(Member member, StudyCategory studyCategory) {
        this.member = member;
        this.studyCategory = studyCategory;
    }

}
