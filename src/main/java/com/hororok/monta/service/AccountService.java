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

@Service
public class AccountService {
    private final AccountRepository accountRepository;


    @Autowired
    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Transactional
    public ResponseEntity<?> saveMember(AccountRequestDto accountRequestDto) {

        // Null 점검
        if(accountRequestDto.getEmail().isEmpty() || accountRequestDto.getPassword().isEmpty() || accountRequestDto.getName().isEmpty()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new FailResponseDto(HttpStatus.BAD_REQUEST.value(), "회원 가입 실패", "빈 값이 존재합니다." ));

        }
        // 이메일 중복 여부 점검
        if(accountRepository.existsByEmail(accountRequestDto.getEmail())) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new FailResponseDto(HttpStatus.INTERNAL_SERVER_ERROR.value(), "회원 가입 실패", "중복된 이메일입니다." ));
        }
        // 이상 없으면 DB에 저장
        Account saveMember = accountRepository.save(new Account(accountRequestDto));
        return ResponseEntity.ok(new AccountResponseDto(saveMember.getId()));

    }
}
