package com.hororok.monta.controller;

import com.hororok.monta.dto.request.shop.PurchaseRequestDto;
import com.hororok.monta.dto.request.shop.SellRequestDto;
import com.hororok.monta.service.ShopService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/v2")
public class ShopController {
    private final ShopService shopService;

    @PostMapping("/shop/purchase")
    public ResponseEntity<?> postPurchaseDetails(@Valid @RequestBody PurchaseRequestDto requestDto) {
        return shopService.addPurchaseDetails(requestDto);
    }

    @PostMapping("/shop/sell")
    public ResponseEntity<?> postSellDetails(@Valid @RequestBody SellRequestDto requestDto) {
        return shopService.addSellDetails(requestDto);
    }
}

