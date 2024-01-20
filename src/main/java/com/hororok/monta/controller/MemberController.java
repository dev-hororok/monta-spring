package com.hororok.monta.controller;

import com.hororok.monta.dto.request.member.PatchMemberRequestDto;
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
    public ResponseEntity<?> getCurrentMember() {
        return memberService.getCurrentMember();
    }

    @GetMapping("/admin/members")
    public ResponseEntity<?> getMembers() {
        return memberService.getMembers();
    }

    @GetMapping("/members/{memberId}")
    public ResponseEntity<?> getMember(@PathVariable UUID memberId) {
        return memberService.getMember(memberId);
    }

    @PostMapping("/members")
    public ResponseEntity<?> postMember() {
        return memberService.postMember();
    }

    @PatchMapping("/members")
    public ResponseEntity<?> patchMember(@RequestBody PatchMemberRequestDto requestDto) {
        return memberService.patchMember(requestDto);
    }

    @DeleteMapping("/members")
    public ResponseEntity<?> deleteMember() {
        return memberService.deleteMember();
    }

}
