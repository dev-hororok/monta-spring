package com.hororok.monta.repository;

import com.hororok.monta.entity.TransactionRecord;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface TransactionRecordTestRepository extends CrudRepository<TransactionRecord, Long> {
    @Transactional
    @Modifying
    @Query(value = "DELETE FROM transaction_record WHERE transaction_record_id = :id", nativeQuery = true)
    void deleteTestData(@Param("id") Long transactionRecordId);
}
