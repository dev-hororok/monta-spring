package com.hororok.monta.repository;

import com.hororok.monta.entity.TransactionRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TransactionRepository extends JpaRepository<TransactionRecord, UUID> {
    boolean existsByMemberId(UUID memberId);
    List<TransactionRecord> findByMemberId(UUID memberId);
    Optional<TransactionRecord> findByIdAndMemberId(long transactionId, UUID memberId);

}
