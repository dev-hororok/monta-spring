package com.hororok.monta.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.UUID;

@Data
@NoArgsConstructor
public class SellRequestDto {
    @JsonProperty("item_type")
    private String itemType;

    @JsonProperty("item_id")
    private UUID itemId;
    private int count;
}