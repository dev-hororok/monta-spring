package com.hororok.monta.dto.response.transaction;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
public class RewardResponseDto {
    private String status;
    private Data data;

    public RewardResponseDto(int rewardPoint, int totalPoint) {
        this.status = "success";
        this.data = new Data(rewardPoint, totalPoint);
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Data {
        @JsonProperty("reward_point")
        private int rewardPoint;

        @JsonProperty("total_point")
        private int totalPoint;
    }
}
