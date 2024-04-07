package com.hororok.monta.dto.request.time;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class ReduceTimeRequestDto {
    @NotNull(message = "시간은 필수 입력 값 입니다.")
    private Integer seconds;
}
