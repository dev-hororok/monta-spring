package com.hororok.monta.dto.response.character;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BasicCharacterResponseDto {
    private int status;
    private Object data;
}