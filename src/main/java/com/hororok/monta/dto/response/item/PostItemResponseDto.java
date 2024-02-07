package com.hororok.monta.dto.response.item;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PostItemResponseDto {

    private String status;
    private Data data;

    public PostItemResponseDto(int itemId) {
        this.status = "success";
        this.data = new Data(itemId);
    }

    @Getter
    @AllArgsConstructor
    public static class Data {
        @JsonProperty("item_id")
        private int itemId;
    }



}
