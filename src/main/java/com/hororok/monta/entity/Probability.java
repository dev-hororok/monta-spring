package com.hororok.monta.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Entity
@Getter
public class Probability extends CommonEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "probability_id")
    private long id;

    @NotBlank
    @Column(length = 10)
    private String eggGrade; // SS, S+, S, A, B, C, AD

    @NotBlank
    @Column(length = 10)
    private String characterGrade; // SS, S+, S, A, B, C
}
