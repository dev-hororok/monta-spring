package com.hororok.monta.dto.response;

import lombok.*;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FailResponseDto {
    private int status;
    private String message;
    private List<String> errors;

}
