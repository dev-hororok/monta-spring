package com.hororok.monta.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
public class Palette extends CommonEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="palette_id")
    private long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    private PaletteGrade grade; // RARE, EPIK, LEGENDARY

    @NotNull
    @Column(length=100)
    private String name;

    @NotNull
    @Column(length=16)
    private String lightColor;

    @NotNull
    @Column(length=16)
    private String normalColor;

    @NotNull
    @Column(length=16)
    private String darkColor;

    @NotNull
    @Column(length=16)
    private String darkerColor;

    @OneToMany(mappedBy = "palette")
    private List<StudyStreak> studyStreaks = new ArrayList<>();

}
