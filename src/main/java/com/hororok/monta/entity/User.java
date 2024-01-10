package com.hororok.monta.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends CommonEntity{

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id", columnDefinition = "BINARY(16)")
    private UUID id;

    @NotBlank
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id")
    private Account account;

    @NotBlank
    @Column(length=100)
    private String nickname;

    @NotBlank @Email
    @Column(length=100)
    private String email;

    private String image_url;

    @NotBlank
    @Enumerated(EnumType.STRING)
    private Authority role;      // ADMIN, USER

    private long active_record_id;

    private long active_egg_id;

    @NotBlank
    private int point;

}
