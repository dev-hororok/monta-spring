package com.hororok.monta.dto.request;

import com.hororok.monta.entity.Authority;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.UUID;

@Getter
@Setter
@Data
public class AccountRequestDto {

    @NotBlank
    @Pattern(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+.[A-Za-z]{2,6}$", message = "올바른 이메일 형식이 아닙니다.")
    @Size(min=5, max=100)
    private String email;

    @NotBlank
    @Size(min=8, max=100)
    private String password;

    @NotBlank
    @Size(min=1, max=100)
    private String name;

}
