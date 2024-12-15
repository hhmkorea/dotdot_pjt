package com.dotdot.site.controller;

import ch.qos.logback.core.model.Model;
import com.dotdot.site.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class UserController {

    @Autowired
    private UserService userService;

    // 로그인
    @GetMapping ("/login.do")
    public String openLogin() {
        return "user/loginForm";
    }

    // 회원가입
    @GetMapping ("/user/join.do")
    public String saveUser() {
        return "user/joinForm";
    }


}
