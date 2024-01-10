package com.hororok.monta.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Entity
@Getter
public class StudyCategory extends CommonEntity{

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="category_id")
    private long id;

    @NotBlank
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @NotBlank
    @Column(length = 50)
    private String subject;

    @NotBlank
    private boolean hidden;
}
