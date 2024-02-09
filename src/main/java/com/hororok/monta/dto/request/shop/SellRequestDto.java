package com.hororok.monta.dto.request.shop;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SellRequestDto {

    @NotNull(message = "필수 입력 값 입니다.")
    @JsonProperty("character_inventory_id")
    private long characterInventoryId;

    @NotNull(message = "필수 입력 값 입니다.")
    private int count;
}
