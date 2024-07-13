package com.hororok.monta.dto.request.transaction;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class RewardRequestDto {
    @NotNull(message = "포인트는 필수 입력 값 입니다.")
    private Integer point;
}
