package com.hororok.monta.controller;

import com.hororok.monta.dto.request.PurchaseRequestDto;
import com.hororok.monta.dto.request.SellRequestDto;
import com.hororok.monta.dto.response.FailResponseDto;
import com.hororok.monta.dto.response.PurchaseResponseDto;
import com.hororok.monta.dto.response.SellResponseDto;
import com.hororok.monta.service.ShopService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;

@RestController
@RequestMapping("/shop")
public class ShopController {
    private final ShopService shopService;

    @Autowired
    public ShopController(ShopService shopService) {
        this.shopService = shopService;
    }

    @PostMapping("/purchase")
    public ResponseEntity<?> purchaseItem(@RequestBody PurchaseRequestDto requestDto) {
        try {
            PurchaseResponseDto responseDto = shopService.purchaseItem(requestDto);
            return ResponseEntity.status(responseDto.getStatus()).body(responseDto);
        } catch (RuntimeException ex){
            FailResponseDto errorResponseDto = new FailResponseDto();
            errorResponseDto.setStatus(HttpStatus.BAD_REQUEST.value());
            errorResponseDto.setMessage("요청을 처리할 수 없습니다.");
            errorResponseDto.setErrors(Collections.singletonList(ex.getMessage()));

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponseDto);
        }
    }

    @PostMapping("/sell")
    public ResponseEntity<?> sellItem(@RequestBody SellRequestDto requestDto) {
        try {
            SellResponseDto responseDto = shopService.sellItem(requestDto);
            return ResponseEntity.status(responseDto.getStatus()).body(responseDto);
        } catch (RuntimeException ex) {
            FailResponseDto errorResponseDto = new FailResponseDto();
            errorResponseDto.setStatus(HttpStatus.BAD_REQUEST.value());
            errorResponseDto.setMessage("판매 요청을 처리할 수 없습니다.");
            errorResponseDto.setErrors(Collections.singletonList(ex.getMessage()));

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponseDto);
        }
    }
}