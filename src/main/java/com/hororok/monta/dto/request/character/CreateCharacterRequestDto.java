package com.hororok.monta.dto.request.character;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CreateCharacterRequestDto {
    @NotBlank(message = "캐릭터 이름은 필수 입력 값 입니다.")
    private String name;

    @NotBlank(message = "캐릭터 설명은 필수 입력 값 입니다.")
    private String description;

    @NotBlank(message = "이미지 URL은 필수 입력 값 입니다.")
    @JsonProperty("image_url")
    private String imageUrl;

    @NotBlank(message = "캐릭터 등급은 필수 입력 값 입니다.")
    private String grade;

    @NotNull(message = "캐릭터 판매 가격은 필수 입력 값 입니다.")
    @JsonProperty("sell_price")
    private int sellPrice;
}
