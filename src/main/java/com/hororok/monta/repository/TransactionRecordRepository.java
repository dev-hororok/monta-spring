package com.hororok.monta.repository;

import com.hororok.monta.entity.TransactionRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface TransactionRecordRepository extends JpaRepository<TransactionRecord, UUID> {

}
