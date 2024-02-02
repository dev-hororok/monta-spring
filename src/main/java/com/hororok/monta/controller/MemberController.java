package com.hororok.monta.controller;

import com.hororok.monta.dto.request.member.PatchMemberRequestDto;
import com.hororok.monta.handler.CustomValidationException;
import com.hororok.monta.service.MemberService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@AllArgsConstructor
public class MemberController {

    private MemberService memberService;

    @GetMapping("/admin/members")
    public ResponseEntity<?> getMembers() {
        return memberService.getMembers();
    }

    @PatchMapping("/members")
    public ResponseEntity<?> patchMember(@Valid @RequestBody PatchMemberRequestDto requestDto, BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            List<String> errors = new ArrayList<>();
            for(FieldError error : bindingResult.getFieldErrors()) {
                errors.add(error.getField() + " : " + error.getDefaultMessage());
            }
            throw new CustomValidationException(errors);
        } else {
            return memberService.patchMember(requestDto);
        }
    }

    @DeleteMapping("/members")
    public ResponseEntity<?> deleteMember() {
        return memberService.deleteMember();
    }

    @PostMapping("/members/{memberId}/egg-inventory/{eggInventoryId}")
    public ResponseEntity<?> postFromEggToCharacter(@PathVariable UUID memberId, @PathVariable UUID eggInventoryId) {
        return memberService.postFromEggToCharacter(memberId, eggInventoryId);
    }


}
