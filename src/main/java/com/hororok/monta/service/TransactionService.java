package com.hororok.monta.service;

import com.hororok.monta.dto.response.transaction.GetAllTransactionRecordResponseDto;
import com.hororok.monta.dto.response.transaction.GetTransactionRecordDto;
import com.hororok.monta.entity.TransactionRecord;
import com.hororok.monta.repository.TransactionRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
public class TransactionService {
    private final TransactionRepository transactionRepository;

    public TransactionService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    public GetAllTransactionRecordResponseDto getAllTransactionRecords(UUID memberId) {
        if (!transactionRepository.existsByMemberId(memberId)){
            throw new RuntimeException("존재하지 않는 멤버 아이디입니다.");
        }

        List<TransactionRecord> transactionRecords = transactionRepository.findByMemberId(memberId);
        List<GetAllTransactionRecordResponseDto.TransactionRecordDto> recordDtos = transactionRecords.stream()
                .map(this::toTransactionRecordDto)
                .collect(Collectors.toList());

        return GetAllTransactionRecordResponseDto.builder()
                .status("success")
                .data(new GetAllTransactionRecordResponseDto.Data(recordDtos))
                .build();
    }

    public Optional<GetTransactionRecordDto> getTransactionRecord(UUID memberId, long transactionId) {
        return transactionRepository.findByIdAndMemberId(transactionId, memberId)
                .map(this::toGetTransactionRecordDto);
    }

    private GetAllTransactionRecordResponseDto.TransactionRecordDto toTransactionRecordDto(TransactionRecord transactionRecord) {
        return GetAllTransactionRecordResponseDto.TransactionRecordDto.builder()
                .transactionRecordId(transactionRecord.getId())
                .memberId(transactionRecord.getMember().getId().toString())
                .transactionType(transactionRecord.getTransactionType().name())
                .amount(transactionRecord.getAmount())
                .count(transactionRecord.getCount())
                .balanceAfterTransaction(transactionRecord.getBalanceAfterTransaction())
                .notes(transactionRecord.getNotes())
                .createdAt(transactionRecord.getCreatedAt())
                .build();
    }

    private GetTransactionRecordDto toGetTransactionRecordDto(TransactionRecord transactionRecord) {
        GetTransactionRecordDto.TransactionRecordDto transactionRecordDto = GetTransactionRecordDto.TransactionRecordDto.builder()
                .transactionRecordId(transactionRecord.getId())
                .memberId(transactionRecord.getMember().getId().toString())
                .transactionType(transactionRecord.getTransactionType().name())
                .amount(transactionRecord.getAmount())
                .count(transactionRecord.getCount())
                .balanceAfterTransaction(transactionRecord.getBalanceAfterTransaction())
                .notes(transactionRecord.getNotes())
                .createdAt(transactionRecord.getCreatedAt())
                .build();

        return GetTransactionRecordDto.builder()
                .status("success")
                .data(new GetTransactionRecordDto.Data(transactionRecordDto))
                .build();
    }
}
