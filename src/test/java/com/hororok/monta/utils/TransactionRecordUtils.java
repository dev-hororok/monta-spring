package com.hororok.monta.utils;

import com.hororok.monta.repository.TransactionRecordTestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TransactionRecordUtils {
    @Autowired
    private TransactionRecordTestRepository transactionRecordTestRepository;

    public void deleteTestData(long transactionId) {
        transactionRecordTestRepository.deleteTestData(transactionId);
    }
}
