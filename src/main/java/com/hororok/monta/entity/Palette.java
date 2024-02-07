package com.hororok.monta.entity;

import com.hororok.monta.dto.request.palette.PostPaletteRequestDto;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Palette extends CommonEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="palette_id")
    private int id;

    @NotNull
    @Column(length=20)
    private String grade; // Rare, Epik, Legendary

    @NotNull
    @Column(length=100)
    private String name;

    @NotNull
    @Column(length=7)
    private String lightColor;

    @NotNull
    @Column(length=7)
    private String normalColor;

    @NotNull
    @Column(length=7)
    private String darkColor;

    @NotNull
    @Column(length=7)
    private String darkerColor;

//    @OneToMany(mappedBy = "palette")
//    private List<StudyStreak> studyStreaks = new ArrayList<>();

    public Palette(PostPaletteRequestDto requestDto) {
        this.name = requestDto.getName();
        this.grade = requestDto.getGrade();
        this.lightColor = requestDto.getLightColor();
        this.normalColor = requestDto.getNormalColor();
        this.darkColor = requestDto.getDarkColor();
        this.darkerColor = requestDto.getDarkerColor();
    }

    public void updatePalette(String name, String grade, String lightColor, String normalColor, String darkColor, String darkerColor) {
        this.name = name;
        this.grade = grade;
        this.lightColor = lightColor;
        this.normalColor = normalColor;
        this.darkColor = darkColor;
        this.darkerColor = darkerColor;
    }

}
