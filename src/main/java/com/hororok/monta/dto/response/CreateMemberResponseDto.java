package com.hororok.monta.dto.response;

import lombok.*;
import org.springframework.http.HttpStatus;

import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateMemberResponseDto {

    private int status;
    private Data data;

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Data{
        private UUID member_id;
    }

    public CreateMemberResponseDto(UUID memberId) {
        this.data = new Data(memberId);
        this.status = HttpStatus.CREATED.value();
    }
}
