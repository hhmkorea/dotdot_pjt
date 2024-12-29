package com.dotdot.site.controller.api;

import com.dotdot.site.model.Member;
import com.dotdot.site.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class MemberApiController {

    @Autowired
    private MemberService memberService;

    // 회원 정보 저장 (회원가입)
    @PostMapping("/saveMember")
    public int saveMember(@RequestBody final Member params) {
        return memberService.saveMember(params);
    }

    // 회원 수 카운팅 (ID 중복 체크)
    @GetMapping("/member-count")
    public int countMemberByUsername(@RequestParam final String username) {
        int count = memberService.countMemberByUsername(username);
        return count;
    }

    // 로그인
    @PostMapping("/login")
    public Member login(@RequestParam final String username, @RequestParam final String password) {

        Member member = memberService.login(username);//, password);

        return member;
    }

}
