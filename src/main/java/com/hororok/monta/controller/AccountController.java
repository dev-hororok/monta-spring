package com.hororok.monta.controller;

import com.hororok.monta.dto.request.PostRegisterRequestDto;
import com.hororok.monta.dto.request.PostLoginRequestDto;
import com.hororok.monta.handler.CustomValidationException;
import com.hororok.monta.jwt.TokenProvider;
import com.hororok.monta.service.AccountService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

import java.util.ArrayList;
import java.util.List;

@RestController
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService, TokenProvider tokenProvider, AuthenticationManagerBuilder authenticationManagerBuilder) {
        this.accountService = accountService;
    }

    @PostMapping("/auth/register")
    public ResponseEntity<?> postRegister(@Valid @RequestBody PostRegisterRequestDto postRegisterRequestDto, BindingResult bindingResult) {
        if(bindingResult.hasErrors()){
            List<String> errors = new ArrayList<>();

            for(FieldError error : bindingResult.getFieldErrors()) {
                errors.add(error.getField() + " : " + error.getDefaultMessage());
            }
            throw new CustomValidationException(errors);
        } else {
            return accountService.postRegister(postRegisterRequestDto);
        }
    }

    @PostMapping("/auth/login")
    public ResponseEntity<?> postLogin(@Valid @RequestBody PostLoginRequestDto postLoginRequestDto, BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            List<String> errors = new ArrayList<>();

            for(FieldError error : bindingResult.getFieldErrors()) {
                errors.add(error.getField() + " : " + error.getDefaultMessage());
            }
            throw new CustomValidationException(errors);
        } else {
            return accountService.postLogin(postLoginRequestDto);
        }
    }
}