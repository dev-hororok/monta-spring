package com.hororok.monta.controller;

import com.hororok.monta.dto.request.transaction.RewardRequestDto;
import com.hororok.monta.service.TransactionService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class TransactionController {
    private final TransactionService transactionService;

    @PostMapping("/transaction/reward")
    public ResponseEntity<?> postReward(@Valid @RequestBody RewardRequestDto requestDto) {
        return transactionService.addReward(requestDto);
    }
}
