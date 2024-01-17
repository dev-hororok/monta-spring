package com.hororok.monta.dto.response;

import lombok.*;
import org.springframework.http.HttpStatus;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponseDto {

    private int status;
    private Data data;

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Data{
        private String access_token;
    }

    public LoginResponseDto(String token) {
        this.data = new Data(token);
        this.status = HttpStatus.OK.value();
    }
}
