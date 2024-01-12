package com.hororok.monta.controller;

import com.hororok.monta.dto.request.AccountRequestDto;
import com.hororok.monta.dto.response.FailResponseDto;
import com.hororok.monta.handler.CustomValidationException;
import com.hororok.monta.jwt.TokenProvider;
import com.hororok.monta.service.AccountService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.Map;

@RestController
public class AccountController {

    private final AccountService accountService;
//    private final TokenProvider tokenProvider;
//    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
//        this.tokenProvider = tokenProvider;
//        this.authenticationManagerBuilder = authenticationManagerBuilder;
    }

    @PostMapping("/auth/register")
    public ResponseEntity<?> register(@Valid @RequestBody AccountRequestDto accountRequestDto, BindingResult bindingResult) {
        if(bindingResult.hasErrors()){
            Map<String, String> errors = new HashMap<>();

            for (FieldError error : bindingResult.getFieldErrors()){
                errors.put(error.getField(), error.getDefaultMessage());
            }
            throw new CustomValidationException("유효성 검사 실패", errors);
        } else {
            return accountService.saveMember(accountRequestDto);
        }
    }
}