package com.dotdot.site.controller;

import com.dotdot.site.config.auth.PrincipalDetails;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class MemberController {

    // 로그인 화면
    @GetMapping({"", "/","/loginForm"})
    public String openLogin() {
        return "views/member/login";
    }

    // 홈 화면
    @GetMapping("/home")
    public String home(Model model, @AuthenticationPrincipal PrincipalDetails principal) {
        model.addAttribute("principal", principal);
        return "home";
    }

    // 로그아웃
    @PostMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login"; // redirect: 사용자가 처음 요청한 URL이 아닌 지정한 URL로 보냄.
    }
}
