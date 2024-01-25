package com.hororok.monta.dto.response;

import lombok.*;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FailResponseDto {
    private String status;
    private String message;
    private List<String> errors;

    public FailResponseDto(String message, List<String> errors) {
        status = "error";
        this.message = message;
        this.errors = errors;
    }

}
