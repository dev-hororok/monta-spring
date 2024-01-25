package com.hororok.monta.dto.response.member;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.springframework.http.HttpStatus;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PostLoginResponseDto {

    private String status;
    private Data data;

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Data{

        @JsonProperty("access_token")
        private String accessToken;
    }

    public PostLoginResponseDto(String token) {
        this.data = new Data(token);
        this.status = "success";
    }
}
