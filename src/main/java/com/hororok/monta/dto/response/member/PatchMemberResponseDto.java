package com.hororok.monta.dto.response.member;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.hororok.monta.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@AllArgsConstructor
public class PatchMemberResponseDto {

    private String status;
    private Data data;

    public PatchMemberResponseDto(Member member) {
        this.status = "success";
        this.data = new Data(new PatchMemberDto(member));
    }

    @Getter
    @AllArgsConstructor
    public static class Data {
        private PatchMemberDto member;
    }


    @Getter
    @AllArgsConstructor
    public static class PatchMemberDto {

        @JsonProperty("member_id")
        private UUID memberId;

        private String email;
        private String nickname;

        @JsonProperty("image_url")
        private String imageUrl;

        private int point;

        @JsonProperty("active_record_id")
        private Long activeRecordId;

        @JsonProperty("active_egg_id")
        private UUID activeEggId;

        @JsonProperty("created_at")
        private LocalDateTime createdAt;

        @JsonProperty("updated_at")
        private LocalDateTime updatedAt;

        public PatchMemberDto(Member member) {
            memberId = member.getId();
            email = member.getEmail();
            nickname = member.getNickname();
            imageUrl = member.getImageUrl();
            point = member.getPoint();
            activeRecordId = member.getActiveRecordId();
            createdAt = member.getCreatedAt();
            updatedAt = member.getUpdatedAt();
        }
    }
}
