package com.hororok.monta.dto.response.itemInventory;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.hororok.monta.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@NoArgsConstructor
public class UsePointGachaResponseDto {
    private String status;
    private Data data;

    public UsePointGachaResponseDto(Member member, int earnedPoint) {
        this.status = "success";
        this.data = new Data("Point Acquisition", new UsePointBoxDto(member, earnedPoint));
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Data {
        private String result;
        private UsePointBoxDto member;
    }

    @Getter
    @NoArgsConstructor
    public static class UsePointBoxDto {
        @JsonProperty("member_id")
        private UUID memberId;

        @JsonProperty("earned_point")
        private int earnedPoint;

        private int point;

        public UsePointBoxDto(Member member, int earnedPoint) {
            this.memberId = member.getId();
            this.earnedPoint = earnedPoint;
            this.point = member.getPoint();
        }
    }
}
