package com.hororok.monta.dto.response;

import lombok.*;
import org.springframework.http.HttpStatus;

import java.util.Map;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FailResponseDto {
    // 공통 에러 응답 Dto
    private int status;
    private String message;
    private Map<String, String> errors;

}
