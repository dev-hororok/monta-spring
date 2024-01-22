package com.hororok.monta.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PurchaseResponseDto {
    private int status;
    private Data data;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Data {
        @JsonProperty("transaction_record")
        private TransactionRecordDto transactionRecord;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class TransactionRecordDto {
        @JsonProperty("transaction_record_id")
        private long transactionRecordId;

        @JsonProperty("transaction_type")
        private String transactionType;
        private int amount;
        private int count;

        @JsonProperty("balance_after_transaction")
        private int balanceAfterTransaction;
        private String notes;
    }
}
