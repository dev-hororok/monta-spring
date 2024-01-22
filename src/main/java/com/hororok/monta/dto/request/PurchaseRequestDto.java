package com.hororok.monta.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
public class PurchaseRequestDto {
    @JsonProperty("item_type")
    private String itemType;

    @JsonProperty("item_id")
    private UUID itemId;

    private int count;
}
