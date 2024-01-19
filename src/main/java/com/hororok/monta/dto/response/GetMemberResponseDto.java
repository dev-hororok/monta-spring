package com.hororok.monta.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public class GetMemberResponseDto {

    private int status;
    private Data data;

    @Getter
    @AllArgsConstructor
    public static class Data {
        private Object member;
    }

    public GetMemberResponseDto(Object member) {
        this.status = HttpStatus.OK.value();
        this.data = new Data(member);
    }
}
