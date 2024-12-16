package com.dotdot.site.controller;

import com.dotdot.site.model.Member;
import com.dotdot.site.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequiredArgsConstructor
public class MemberController {

    //@Autowired
    //private AuthenticationManager authenticationManager;

    @Autowired
    private MemberService memberService;

    // 로그인
    @GetMapping ("/login.do")
    public String loginForm() {
        return "member/loginForm";
    }

    // 회원 정보 저장 (회원가입)
    @PostMapping("/saveMember.do")
    @ResponseBody
    public int saveMember(@RequestBody final Member params) {
        return memberService.saveMember(params);
    }

}
