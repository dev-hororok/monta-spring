package com.hororok.monta.dto.response.member;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.hororok.monta.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@Getter
@AllArgsConstructor
public class GetMemberDto {

    @JsonProperty("member_id")
    private UUID memberId;
    private String nickname;

    @JsonProperty("image_url")
    private String imageUrl;

    public GetMemberDto(Member member) {
        memberId = member.getId();
        nickname = member.getNickname();
        imageUrl = member.getImageUrl();
    }
}
