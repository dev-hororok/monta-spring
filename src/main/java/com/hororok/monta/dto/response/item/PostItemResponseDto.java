package com.hororok.monta.dto.response.item;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.hororok.monta.dto.response.member.PatchMemberResponseDto;
import com.hororok.monta.entity.Item;
import com.hororok.monta.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@AllArgsConstructor
public class PostItemResponseDto {

    private String status;
    private Data data;

    public PostItemResponseDto(Long itemId) {
        this.status = "success";
        this.data = new Data(itemId);
    }

    @Getter
    @AllArgsConstructor
    public static class Data {
        @JsonProperty("item_id")
        private long itemId;
    }



}
