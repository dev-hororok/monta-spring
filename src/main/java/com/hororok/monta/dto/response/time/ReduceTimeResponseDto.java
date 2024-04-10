package com.hororok.monta.dto.response.time;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ReduceTimeResponseDto {
    private String status;

    public ReduceTimeResponseDto() {
        this.status = "success";
    }
}
