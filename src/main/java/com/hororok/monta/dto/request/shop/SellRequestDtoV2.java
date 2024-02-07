package com.hororok.monta.dto.request.shop;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@Getter
@AllArgsConstructor
public class SellRequestDtoV2 {

    @NotNull(message = "필수 입력 값 입니다.")
    @JsonProperty("character_id")
    private int characterId;

    @NotNull(message = "필수 입력 값 입니다.")
    private int count;
}
