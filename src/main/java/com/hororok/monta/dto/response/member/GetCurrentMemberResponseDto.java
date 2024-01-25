package com.hororok.monta.dto.response.member;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.hororok.monta.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@AllArgsConstructor
public class GetCurrentMemberResponseDto {

    private String status;
    private Data data;

    public GetCurrentMemberResponseDto(Member member) {
        this.status = "success";
        this.data = new Data(new GetCurrentMemberDto(member));
    }

    @Getter
    @AllArgsConstructor
    public static class Data {
        private GetCurrentMemberDto member;
    }

    @Getter
    @AllArgsConstructor
    public static class GetCurrentMemberDto {
        @JsonProperty("member_id")
        private UUID memberId;
        private String email;
        private String nickname;

        @JsonProperty("image_url")
        private String imageUrl;
        private int point;

        @JsonProperty("active_record_id")
        private long activeRecordId;

        @JsonProperty("active_egg_id")
        private UUID activeEggId;

        @JsonProperty("created_at")
        private LocalDateTime createdAt;

        @JsonProperty("updated_at")
        private LocalDateTime updatedAt;

        public GetCurrentMemberDto(Member member) {
            memberId = member.getId();
            email = member.getEmail();
            nickname = member.getNickname();
            imageUrl = member.getImageUrl();
            point = member.getPoint();
            activeRecordId = member.getActiveRecordId();
            activeEggId = member.getActiveEggId();
            createdAt = member.getCreatedAt();
            updatedAt = member.getUpdatedAt();
        }
    }
}
