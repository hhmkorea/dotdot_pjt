package com.dotdot.site.controller;

import com.dotdot.site.service.BoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

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
    public String openList(Model model, @PageableDefault(size = 5, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        model.addAttribute("boards", boardService.viewList(pageable));
        return "views/board/list";
    }

    @GetMapping("/board/{id}")
    public String findById(@PathVariable int id, Model model) {
        model.addAttribute("board", boardService.viewDetail(id));
        return "views/board/detail";
    }

    @GetMapping("/board/{id}/update")
    public String updateForm(@PathVariable int id, Model model) {
        model.addAttribute("board", boardService.viewDetail(id));
        return "views/board/update";
    }

    // USER 권한이 필요
    @GetMapping("/board/write")
    public String saveForm() {
        return "views/board/write";
    }
}
