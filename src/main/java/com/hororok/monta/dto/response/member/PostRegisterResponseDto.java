package com.hororok.monta.dto.response.member;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.springframework.http.HttpStatus;

import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PostRegisterResponseDto {

    private String status;
    private Data data;

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Data{
        @JsonProperty("account_id")
        private UUID accountId;
    }

    public PostRegisterResponseDto(UUID accountId) {
        this.data = new Data(accountId);
        this.status = "success";
    }
}