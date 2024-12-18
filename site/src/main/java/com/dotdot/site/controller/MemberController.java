package com.dotdot.site.controller;

import com.dotdot.site.model.Member;
import com.dotdot.site.service.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class MemberController {

    //@Autowired
    //private AuthenticationManager authenticationManager;

    @Autowired
    private MemberService memberService;

    // 로그인 화면
    @GetMapping("/login.do")
    public String openLogin() {
        return "member/loginForm";
    }

    // 홈 화면
    @GetMapping("/home.do")
    public String home() {
        return "home";
    }

    // 회원 정보 저장 (회원가입)
    @PostMapping("/saveMember")
    @ResponseBody
    public int saveMember(@RequestBody final Member params) {
        return memberService.saveMember(params);
    }

    // 회원 수 카운팅 (ID 중복 체크)
    @GetMapping("/member-count")
    @ResponseBody
    public int countMemberByUsername(@RequestParam final String username) {
        int count = memberService.countMemberByUsername(username);
        return count;
    }

    // 로그인
    @PostMapping("/login")
    @ResponseBody
    public Member login(HttpServletRequest request) {

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

    // 로그아웃
    @PostMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login.do"; // redirect: 사용자가 처음 요청한 URL이 아닌 지정한 URL로 보냄.
    }
}
