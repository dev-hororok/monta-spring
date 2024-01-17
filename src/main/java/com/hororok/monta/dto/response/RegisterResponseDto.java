package com.hororok.monta.dto.response;
import lombok.*;
import org.springframework.http.HttpStatus;

import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterResponseDto {

    private int status;
    private Data data;

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Data{
        private UUID account_id;
    }

    public RegisterResponseDto(UUID account_id) {
        this.data = new Data(account_id);
        this.status = HttpStatus.CREATED.value();
    }
}