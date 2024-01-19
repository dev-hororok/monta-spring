package com.hororok.monta.controller;

import com.hororok.monta.service.MemberService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

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

}
