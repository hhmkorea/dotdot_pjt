package com.dotdot.board.controller;

import com.dotdot.board.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class BoardController {

    @Autowired
    private BoardService boardService;

//    @GetMapping({"", "/"})
//    public String index() {
//        return "index";
//    }

    // 게시글 리스트 페이지
    @GetMapping ("/board/list.do")
    public String openList() {
        return "board/listForm";
    }


}
