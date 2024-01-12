package com.hororok.monta.dto.response;

import lombok.*;
import org.springframework.http.HttpStatus;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FailResponseDto {

    private int status;
    private String message;
    private String errors;

}
