package com.hororok.monta.dto.request.shop;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PurchaseRequestDto {

    @NotNull(message = "필수 입력 값 입니다.")
    @JsonProperty("item_id")
    private int itemId;

    @NotNull(message = "필수 입력 값 입니다.")
    private int count;
}
