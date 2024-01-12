package com.hororok.monta.handler;

import com.hororok.monta.dto.response.FailResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

@RestController
@ControllerAdvice
public class ControllerExceptionHandler {
    @ExceptionHandler(CustomValidationException.class)
    public FailResponseDto validationException(CustomValidationException e){
        return new FailResponseDto(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage(), e.getErrors());
    }
}