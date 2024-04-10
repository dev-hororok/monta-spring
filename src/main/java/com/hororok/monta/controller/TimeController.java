package com.hororok.monta.controller;

import com.hororok.monta.dto.request.time.ReduceTimeRequestDto;
import com.hororok.monta.handler.CustomValidationException;
import com.hororok.monta.service.TimeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class TimeController {
    private final TimeService timeService;

    @PostMapping("/item-inventory/apply-time")
    public ResponseEntity<?> reduceTime(@Valid @RequestBody ReduceTimeRequestDto requestDto, BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            List<String> errors = new ArrayList<>();
            for(FieldError error : bindingResult.getFieldErrors()) {
                errors.add(error.getField() + " : " + error.getDefaultMessage());
            }
            throw new CustomValidationException(errors);
        } else {
            return timeService.reduceTime(requestDto);
        }
    }
}
