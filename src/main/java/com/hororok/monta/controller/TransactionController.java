package com.hororok.monta.controller;

import com.hororok.monta.dto.response.FailResponseDto;
import com.hororok.monta.dto.response.transaction.GetAllTransactionRecordResponseDto;
import com.hororok.monta.dto.response.transaction.GetTransactionRecordDto;
import com.hororok.monta.service.TransactionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/members")
public class TransactionController {
    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService){
        this.transactionService = transactionService;
    }

    @GetMapping("/{member_id}/transaction-records")
    public ResponseEntity<?> getAllTransactionRecords(@PathVariable("member_id") UUID memberId) {
        try {
            GetAllTransactionRecordResponseDto responseDto = transactionService.getAllTransactionRecords(memberId);
            return ResponseEntity.ok(responseDto);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new FailResponseDto());
        }
    }

    @GetMapping("/{member_id}/transaction-records/{transaction_id}")
    public ResponseEntity<?> getTransactionRecord(@PathVariable("member_id") UUID memberId,
                                                  @PathVariable("transaction_id") long transactionId) {
        Optional<GetTransactionRecordDto> responseDto = transactionService.getTransactionRecord(memberId, transactionId);
        if (responseDto.isPresent()) {
            return ResponseEntity.ok(responseDto.get());
        } else {
            FailResponseDto failResponse = FailResponseDto.builder()
                    .message("요청을 처리할 수 없습니다.")
                    .errors(Collections.singletonList("요청하신 id가 존재하지 않습니다."))
                    .build();
            return new ResponseEntity<>(failResponse, HttpStatus.NOT_FOUND);
        }
    }
}
