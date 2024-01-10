package com.hororok.monta.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.UUID;

@Entity
@Getter
public class Account extends CommonEntity{

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @JdbcTypeCode(SqlTypes.VARCHAR)
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

    @NotNull
    @Enumerated(EnumType.STRING)
    private Authority role;

    @OneToOne(mappedBy = "account", cascade = CascadeType.ALL, orphanRemoval = true)
    private Member member;

}
