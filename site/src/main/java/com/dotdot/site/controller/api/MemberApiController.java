package com.dotdot.site.controller.api;

import com.dotdot.site.model.Member;
import com.dotdot.site.service.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
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
    public Member loginProc(HttpServletRequest request) {

        // 1. 회원 정보 조회
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        Member member = memberService.login(username);//, password);

        // 2. 세션에 회원 정보 저장 & 세션 유지 시간 설정
        if ( member != null ) {
            HttpSession session = request.getSession();;
            session.setAttribute("loginMember", member); // body.html에 session.loginMember 로 session 연결시 로컬 저장소에 저장되어 있음.
            session.setMaxInactiveInterval(60 * 30); // 1800초 = 30분
        }

        return member;
    }

}
