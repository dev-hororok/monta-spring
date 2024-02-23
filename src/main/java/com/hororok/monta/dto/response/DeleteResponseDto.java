package com.hororok.monta.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class DeleteResponseDto {
    private String status;

    public DeleteResponseDto() {
        this.status = "success";
    }
}
