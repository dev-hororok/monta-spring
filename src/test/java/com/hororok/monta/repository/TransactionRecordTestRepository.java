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
    @Query("DELETE FROM TransactionRecord t WHERE t.id = :id")
    void deleteTestData(@Param("id") Long transactionRecordId);
}
