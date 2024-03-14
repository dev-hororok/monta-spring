package com.hororok.monta.controller;

import com.hororok.monta.service.MemberService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
public class MemberController {
    private MemberService memberService;

    @GetMapping("/admin/members")
    public ResponseEntity<?> getMemberList() {
        return memberService.getMembers();
    }
}
