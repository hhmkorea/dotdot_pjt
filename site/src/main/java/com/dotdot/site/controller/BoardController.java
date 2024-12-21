package com.dotdot.site.controller;

import com.dotdot.site.service.BoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller // View 화면(*.html) 리턴
public class BoardController {

    @Autowired
    private BoardService boardService;

//    @GetMapping({"", "/"})
//    public String index() {
//        return "index";
//    }

    // 게시글 리스트 페이지
    @GetMapping ("/board/info")
    public String openInfo() {
        return "views/board/info";
    }

    // 게시글 리스트 페이지
    @GetMapping ("/board/list")
    public String openList() {
        return "views/board/list";
    }

    // USER 권한이 필요
    @GetMapping("/board/write")
    public String saveForm() {
        return "views/board/write";
    }
}
