package com.hororok.monta.controller;

import com.hororok.monta.dto.request.UpdateMemberRequestDto;
import com.hororok.monta.service.MemberService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@AllArgsConstructor
public class MemberController {

    private MemberService memberService;

    @GetMapping("/me")
    public ResponseEntity<?> currentMember() {
        return memberService.currentMember();
    }

    @GetMapping("/admin/members")
    public ResponseEntity<?> getMembers() {
        return memberService.getMembers();
    }

    @PostMapping("/members")
    public ResponseEntity<?> createMember() {
        return memberService.createMember();
    }

    @PatchMapping("/members")
    public ResponseEntity<?> updateMember(@RequestBody UpdateMemberRequestDto requestDto) {
        return memberService.updateMember(requestDto);
    }

    @DeleteMapping("/members")
    public ResponseEntity<?> deleteMember() {
        return memberService.deleteMember();
    }

}
