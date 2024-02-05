package com.hororok.monta.dto.response.shop;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.hororok.monta.entity.TransactionRecord;
import lombok.*;

@Getter
@AllArgsConstructor
public class TransactionResponseDtoV2 {

    private String status;
    private Data data;

    public TransactionResponseDtoV2(TransactionRecord transactionRecord) {
        this.status = "success";
        this.data = new Data(new PurchaseDtoV2(transactionRecord));
    }

    @Getter
    @AllArgsConstructor
    public static class Data {
        @JsonProperty("transaction_record")
        private PurchaseDtoV2 transactionRecord;
    }

    @Getter
    @NoArgsConstructor
    public static class PurchaseDtoV2 {

        @JsonProperty("transaction_record_id")
        private Long transactionRecordId;

        @JsonProperty("transaction_type")
        private String transactionType;

        private int amount;
        private int count;

        @JsonProperty("balance_after_transaction")
        private int balanceAfterTransaction;

        private String notes;

        public PurchaseDtoV2(TransactionRecord transactionRecord) {
            this.transactionRecordId = transactionRecord.getId();
            this.transactionType = transactionRecord.getTransactionType();
            this.amount = transactionRecord.getAmount();
            this.count = transactionRecord.getCount();
            this.balanceAfterTransaction = transactionRecord.getBalanceAfterTransaction();
            this.notes = transactionRecord.getNotes();
        }
    }
}
