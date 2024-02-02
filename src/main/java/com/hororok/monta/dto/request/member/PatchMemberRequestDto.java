package com.hororok.monta.dto.request.member;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class PatchMemberRequestDto {

    @NotBlank(message = "nickname 필수 입력 값 입니다.")
    private String nickname;

    @JsonProperty("image_url")
    private String imageUrl;

}
