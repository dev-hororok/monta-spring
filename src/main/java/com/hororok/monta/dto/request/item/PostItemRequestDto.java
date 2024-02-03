package com.hororok.monta.dto.request.item;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class PostItemRequestDto {

    @NotBlank(message = "필수 입력 값 입니다.")
    @JsonProperty("item_type")
    private String itemType;

    @NotBlank(message = "필수 입력 값 입니다.")
    private String name;

    private String grade;

    @NotBlank(message = "필수 입력 값 입니다.")
    private String description;

    @NotBlank(message = "필수 입력 값 입니다.")
    @JsonProperty("image_url")
    private String imageUrl;

    @NotNull(message = "필수 입력 값 입니다.")
    private int cost;

    @JsonProperty("required_study_time")
    private Integer requiredStudyTime;

    @NotNull(message = "필수 입력 값 입니다.")
    @JsonProperty("effect_code")
    private int effectCode;

    @NotNull(message = "필수 입력 값 입니다.")
    @JsonProperty("is_hidden")
    private Boolean isHidden;
}
