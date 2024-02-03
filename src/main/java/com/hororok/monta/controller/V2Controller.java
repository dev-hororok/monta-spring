package com.hororok.monta.controller;

import com.hororok.monta.dto.request.item.PatchItemRequestDto;
import com.hororok.monta.dto.request.item.PostItemRequestDto;
import com.hororok.monta.handler.CustomValidationException;
import com.hororok.monta.service.V2Service;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/v2")
@AllArgsConstructor
public class V2Controller {

    private final V2Service v2Service;

    @PostMapping("/admin/items")
    public ResponseEntity<?> postItem(@Valid @RequestBody PostItemRequestDto requestDto, BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            List<String> errors = new ArrayList<>();
            for(FieldError error : bindingResult.getFieldErrors()) {
                errors.add(error.getField() + " : " + error.getDefaultMessage());
            }
            throw new CustomValidationException(errors);
        } else {
            return v2Service.postItem(requestDto);
        }
    }

    @GetMapping("/admin/items")
    public ResponseEntity<?> getItems() {
        return v2Service.getItems();
    }

    @GetMapping("/admin/items/{itemId}")
    public ResponseEntity<?> getItem(@PathVariable Long itemId) {
        return v2Service.getItem(itemId);
    }

    @PatchMapping ("/admin/items/{itemId}")
    public ResponseEntity<?> patchItem(@Valid @RequestBody PatchItemRequestDto requestDto, @PathVariable Long itemId) {
        return v2Service.patchItem(requestDto, itemId);
    }

    @DeleteMapping ("/admin/items/{itemId}")
    public ResponseEntity<?> deleteItem(@PathVariable Long itemId) {
        return v2Service.deleteItem(itemId);
    }

}
