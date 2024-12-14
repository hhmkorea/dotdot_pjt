package com.dotdot.board.controller;

import com.dotdot.board.service.BoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class BoardController {

    @Autowired
    private BoardService boardService;

    @GetMapping({"", "/"})
    public String index() {
        return "index";
    }

    @GetMapping ("/login.do") // 로그인 화면
    public String loginForm() {
        return "user/loginForm";
    }
}
