package com.hororok.monta.entity;

import com.hororok.monta.dto.request.member.PostRegisterRequestDto;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.UUID;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Account extends CommonEntity{

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @JdbcTypeCode(SqlTypes.VARCHAR)
    @Column(name="account_id")
    private UUID id;

    @NotNull
    @Email
    @Column(length = 100)
    private String email;

    @NotNull
    @Column(length = 100)
    private String password;

    @NotNull
    @Column(length = 100)
    private String name;

    @NotNull
    @Enumerated(EnumType.STRING)
    private Authority role;

    @OneToOne(mappedBy = "account", cascade = CascadeType.ALL, orphanRemoval = true)
    private Member member;

    public Account(PostRegisterRequestDto postRegisterRequestDto) {
        this.email = postRegisterRequestDto.getEmail();
        this.password = postRegisterRequestDto.getPassword();
        this.name = postRegisterRequestDto.getName();
        this.role = Authority.USER;
    }

}
