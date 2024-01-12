package com.hororok.monta.service;

import com.hororok.monta.dto.request.AccountRequestDto;
import com.hororok.monta.dto.response.AccountResponseDto;
import com.hororok.monta.dto.response.FailResponseDto;
import com.hororok.monta.entity.Account;
import com.hororok.monta.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

@Service
public class AccountService {
    private final AccountRepository accountRepository;


    @Autowired
    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Transactional
    public ResponseEntity<?> saveMember(AccountRequestDto accountRequestDto) {

        // 이메일 중복 여부 점검
        if(accountRepository.existsByEmail(accountRequestDto.getEmail())) {
            Map<String, String> errors = new HashMap<>();
            errors.put("email", "중복된 이메일입니다."); // 중복된 이메일에 대한 에러 메시지를 Map에 추가

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new FailResponseDto(HttpStatus.INTERNAL_SERVER_ERROR.value(), "회원 가입 실패", errors ));
        }

        // 이상 없으면 DB에 저장
        Account saveMember = accountRepository.save(new Account(accountRequestDto));
        return ResponseEntity.ok(new AccountResponseDto(saveMember.getId()));

    }
}
