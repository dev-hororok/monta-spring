package com.hororok.monta.dto.request;

import com.hororok.monta.entity.Authority;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AccountRequestDto {

    @NotNull
    @Email
    @Size(min=5, max=100)
    private String email;

    @NotNull
    @Size(min=8, max=100)
    private String password;

    @NotNull
    @Size(min=1, max=100)
    private String name;

}
