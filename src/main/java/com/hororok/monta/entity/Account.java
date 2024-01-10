package com.hororok.monta.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

import java.util.UUID;

@Entity
@Getter
public class Account extends CommonEntity{

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="account_id")
    private UUID id;

    @NotBlank
    @Email
    @Column(length = 100)
    private String email;

    @NotBlank
    @Column(length = 100)
    private String password;

    @NotBlank
    @Column(length = 100)
    private String name;

    @NotBlank
    @Enumerated(EnumType.STRING)
    private Authority role;

    @OneToOne(mappedBy = "account", cascade = CascadeType.ALL, orphanRemoval = true)
    private Member member;

}
