package com.hororok.monta.dto.response.transaction;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GetTransactionRecordDto {
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

        @JsonProperty("member_id")
        private String memberId;

        @JsonProperty("transaction_type")
        private String transactionType;
        private int amount;
        private int count;

        @JsonProperty("balance_after_transaction")
        private int balanceAfterTransaction;
        private String notes;

        @JsonProperty("created_at")
        private Date createdAt;
    }
}
