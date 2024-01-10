package com.hororok.monta.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Entity
@Getter
public class StreakColorChangePermission extends CommonEntity{

    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="streak_permission_id")
    private long id;

    @NotBlank
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    private int available_change;

}
