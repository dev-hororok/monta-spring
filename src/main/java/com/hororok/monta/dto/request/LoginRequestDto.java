package com.hororok.monta.dto.request;

import lombok.*;

import jakarta.validation.constraints.NotNull;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequestDto {
    @NotNull
    private String email;

    @NotNull
    private String password;
}