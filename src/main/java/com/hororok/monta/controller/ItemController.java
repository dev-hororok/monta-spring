package com.hororok.monta.controller;

import com.hororok.monta.dto.request.item.UpdateItemRequestDto;
import com.hororok.monta.dto.request.item.CreateItemRequestDto;
import com.hororok.monta.handler.CustomValidationException;
import com.hororok.monta.service.ItemService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/v2")
public class ItemController {
    private final ItemService itemService;

    @PostMapping("/admin/items")
    public ResponseEntity<?> postItemDetails(@Valid @RequestBody CreateItemRequestDto requestDto, BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            List<String> errors = new ArrayList<>();
            for(FieldError error : bindingResult.getFieldErrors()) {
                errors.add(error.getField() + " : " + error.getDefaultMessage());
            }
            throw new CustomValidationException(errors);
        } else {
            return itemService.addItemDetails(requestDto);
        }
    }

    @GetMapping("/admin/items")
    public ResponseEntity<?> getItemList() {
        return itemService.findItemList();
    }

    @GetMapping("/admin/items/{itemId}")
    public ResponseEntity<?> getItemDetails(@PathVariable int itemId) {
        return itemService.findItemDetails(itemId);
    }

    @PatchMapping ("/admin/items/{itemId}")
    public ResponseEntity<?> patchItemDetails(@Valid @RequestBody UpdateItemRequestDto requestDto, @PathVariable int itemId, BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            List<String> errors = new ArrayList<>();
            for(FieldError error : bindingResult.getFieldErrors()) {
                errors.add(error.getField() + " : " + error.getDefaultMessage());
            }
            throw new CustomValidationException(errors);
        } else {
            return itemService.updateItemDetails(requestDto, itemId);
        }
    }

    @DeleteMapping ("/admin/items/{itemId}")
    public ResponseEntity<?> deleteItem(@PathVariable int itemId) {
        return itemService.deleteItem(itemId);
    }

    @PostMapping("/item-inventory/{itemInventoryId}")
    public ResponseEntity<?> useItem(@PathVariable Long itemInventoryId) {
        return itemService.useItem(itemInventoryId);
    }
}
