package com.dotdot.board.controller;

import ch.qos.logback.core.model.Model;
import com.dotdot.board.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UserController {

    @Autowired
    private UserService userService;

    // 로그인
    @GetMapping ("/user/login.do")
    public String loginForm() {
        return "user/loginForm";
    }

    // 회원가입
    @GetMapping ("/user/join.do")
    public String joinForm() {
        return "user/joinForm";
    }


}
