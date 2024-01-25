package com.hororok.monta.dto.response.character;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BasicCharacterResponseDto {
    private String status;
    private Object data;
}