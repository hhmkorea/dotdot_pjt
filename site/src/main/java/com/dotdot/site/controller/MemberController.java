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

    // 로그인 화면
    @GetMapping({"", "/","/login"})
    public String openLogin() {
        return "views/member/login";
    }

    // 홈 화면
    @GetMapping("/home")
    public String home() {
        return "home";
    }

    // 로그아웃
    @PostMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login"; // redirect: 사용자가 처음 요청한 URL이 아닌 지정한 URL로 보냄.
    }
}
