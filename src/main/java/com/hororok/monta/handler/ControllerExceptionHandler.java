package com.hororok.monta.handler;

import com.hororok.monta.dto.response.FailResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;

@RestController
@ControllerAdvice
public class ControllerExceptionHandler {
    @ExceptionHandler(CustomValidationException.class)
    public ResponseEntity<FailResponseDto> validationException(CustomValidationException e){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new FailResponseDto(HttpStatus.BAD_REQUEST.name(), e.getErrors()));
    }
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<FailResponseDto> formatException(HttpMessageNotReadableException e){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new FailResponseDto(HttpStatus.BAD_REQUEST.name(), Collections.singletonList("입력한 필드 타입이 올바른지 확인해주세요.")));
    }
}