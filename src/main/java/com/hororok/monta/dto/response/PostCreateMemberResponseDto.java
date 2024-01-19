package com.hororok.monta.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.springframework.http.HttpStatus;

import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PostCreateMemberResponseDto {

    private int status;
    private Data data;

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Data{

        @JsonProperty("member_id")
        private UUID memberId;
    }

    public PostCreateMemberResponseDto(UUID memberId) {
        this.data = new Data(memberId);
        this.status = HttpStatus.CREATED.value();
    }
}
