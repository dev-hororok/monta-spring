package com.hororok.monta.dto.response;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BasicResponseDto {
    private int status;
    private Object data;
}