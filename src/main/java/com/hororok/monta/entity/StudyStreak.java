package com.hororok.monta.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Entity
@Getter
public class StudyStreak {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "streak_id")
    private long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "palette_id")
    private Palette palette;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @NotBlank
    private int currentStreak;

    @NotBlank
    private int longestStreak;
}
