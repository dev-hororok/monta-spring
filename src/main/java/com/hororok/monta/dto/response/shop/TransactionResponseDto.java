package com.hororok.monta.dto.response.shop;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.hororok.monta.entity.TransactionRecord;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
public class TransactionResponseDto {
    private String status;
    private Data data;

    public TransactionResponseDto(TransactionRecord transactionRecord) {
        this.status = "success";
        this.data = new Data(new TransactionDto(transactionRecord));
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Data {
        @JsonProperty("transaction_record")
        private TransactionDto transactionRecord;
    }

    @Getter
    @NoArgsConstructor
    public static class TransactionDto {

        @JsonProperty("transaction_record_id")
        private Long transactionRecordId;

        @JsonProperty("transaction_type")
        private String transactionType;

        private int amount;
        private int count;

        @JsonProperty("balance_after_transaction")
        private int balanceAfterTransaction;

        private String notes;

        public TransactionDto(TransactionRecord transactionRecord) {
            this.transactionRecordId = transactionRecord.getId();
            this.transactionType = transactionRecord.getTransactionType();
            this.amount = transactionRecord.getAmount();
            this.count = transactionRecord.getCount();
            this.balanceAfterTransaction = transactionRecord.getBalanceAfterTransaction();
            this.notes = transactionRecord.getNotes();
        }
    }
}
