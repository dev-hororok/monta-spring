package com.hororok.monta.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class StreakColorChangePermission extends CommonEntity{

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="streak_color_change_permission_id")
    private long id;

    @NotNull
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    private int availableChange;
}
