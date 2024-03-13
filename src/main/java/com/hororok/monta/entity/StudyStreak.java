package com.hororok.monta.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@Entity
@Getter
@NoArgsConstructor
@SQLDelete(sql = "UPDATE study_streak SET deleted_at = CURRENT_TIMESTAMP WHERE study_streak_id = ?")
@Where(clause = "deleted_at IS NULL")
public class StudyStreak extends CommonEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "study_streak_id")
    private int id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "palette_id")
    private Palette palette;

    @NotNull
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @NotNull
    private int currentStreak;

    @NotNull
    private int longestStreak;

    public StudyStreak(Palette palette, Member member) {
        this.palette = palette;
        this.member = member;
        currentStreak = 0;
        longestStreak = 0;
    }

    public void updatePalette(Palette palette) {
        this.palette = palette;
    }
}
