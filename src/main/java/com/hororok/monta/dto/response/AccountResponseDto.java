package com.hororok.monta.dto.response;

import lombok.*;
import org.springframework.http.HttpStatus;

import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AccountResponseDto {

    private int status;
//    private Data data;
    private UUID account_id;

//    @AllArgsConstructor
//    public static class Data{
//        private UUID account_id;
//    }

    public AccountResponseDto(UUID account_id) {
        this.account_id = account_id;
        this.status = HttpStatus.CREATED.value();
    }
}
