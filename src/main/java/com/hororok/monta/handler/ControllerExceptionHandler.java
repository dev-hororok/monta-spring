package com.hororok.monta.handler;

import com.hororok.monta.dto.response.FailResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

@RestController
@ControllerAdvice
public class ControllerExceptionHandler {
    @ExceptionHandler(CustomValidationException.class)
    public ResponseEntity<FailResponseDto> validationException(CustomValidationException e){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new FailResponseDto("유효성 검사 실패", e.getErrors()));
    }
}