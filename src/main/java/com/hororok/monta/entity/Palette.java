package com.hororok.monta.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Entity
@Getter
public class Palette extends CommonEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="palette_id")
    private long id;

    @NotBlank
    @Enumerated(EnumType.STRING)
    private PaletteGrade paletteGrade; // RARE, EPIK, LEGENDARY

    @NotBlank
    @Column(length=100)
    private String name;

    @NotBlank
    @Column(length=16)
    private String lightColor;

    @NotBlank
    @Column(length=16)
    private String normalColor;

    @NotBlank
    @Column(length=16)
    private String darkColor;

    @NotBlank
    @Column(length=16)
    private String darkerColor;
}
