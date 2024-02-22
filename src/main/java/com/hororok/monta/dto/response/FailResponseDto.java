package com.hororok.monta.dto.response;

import lombok.*;
import java.util.List;

@Getter
@NoArgsConstructor
public class FailResponseDto {
    private String status;
    private String error;
    private List<String> message;

    public FailResponseDto(String error, List<String> message) {
        status = "error";
        this.error = error;
        this.message = message;
    }
}
