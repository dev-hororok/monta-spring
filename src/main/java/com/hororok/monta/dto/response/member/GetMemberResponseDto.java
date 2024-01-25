package com.hororok.monta.dto.response.member;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.hororok.monta.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.util.UUID;

@Getter
@AllArgsConstructor
public class GetMemberResponseDto {

    private String status;
    private Data data;

    public GetMemberResponseDto(Member member) {
        this.status = "success";
        this.data = new Data(new GetMemberDto(member));
    }

    @Getter
    @AllArgsConstructor
    public static class Data {
        private GetMemberDto member;
    }


    @Getter
    @AllArgsConstructor
    public static class GetMemberDto {

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
}
