package com.hororok.monta.dto.response.member;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.util.List;

@Getter
@AllArgsConstructor
public class GetMembersResponseDto {

    private int status;
    private Data data;

    @Getter
    @AllArgsConstructor
    public static class Data {
        private List<GetMembersDto> members;
    }

    public GetMembersResponseDto(List<GetMembersDto> member) {
        this.status = HttpStatus.OK.value();
        this.data = new Data(member);
    }
}
