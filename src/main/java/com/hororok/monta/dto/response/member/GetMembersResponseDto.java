package com.hororok.monta.dto.response.member;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.hororok.monta.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@AllArgsConstructor
public class GetMembersResponseDto {

    private String status;
    private Data data;

    public GetMembersResponseDto(List<Member> members) {
        this.status = "success";
        this.data = new Data(convertToDtoList(members));
    }

    private List<GetMembersDto> convertToDtoList(List<Member> members) {
        List<GetMembersDto> list = new ArrayList<>();
        for(Member member : members) {
            list.add(new GetMembersDto(member));
        }
        return list;
    }

    @Getter
    @AllArgsConstructor
    public static class Data {
        private List<GetMembersDto> members;
    }

    @Getter
    @AllArgsConstructor
    public static class GetMembersDto {

        @JsonProperty("member_id")
        private UUID memberId;

        @JsonProperty("account_id")
        private UUID accountId;

        private String nickname;

        @JsonProperty("image_url")
        private String imageUrl;
        private int point;

        @JsonProperty("status_message")
        private String statusMessage;

        @JsonProperty("active_record_id")
        private Long activeRecordId;

        @JsonProperty("created_at")
        private LocalDateTime createdAt;

        @JsonProperty("updated_at")
        private LocalDateTime updatedAt;

        public GetMembersDto(Member member) {
            memberId = member.getId();
            accountId = member.getAccount().getId();
            nickname = member.getNickname();
            imageUrl = member.getImageUrl();
            point = member.getPoint();
            statusMessage = member.getStatusMessage();
            activeRecordId = member.getActiveRecordId();
            createdAt = member.getCreatedAt();
            updatedAt = member.getUpdatedAt();
        }
    }
}
