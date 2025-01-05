package com.dotdot.site.controller;

import com.dotdot.site.config.auth.PrincipalDetails;
import com.dotdot.site.model.Board;
import com.dotdot.site.service.BoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller // View 화면(*.html) 리턴
@RequestMapping("/board")
public class BoardController {

    @Autowired
    private BoardService boardService;

    // 게시글 리스트 페이지
    @GetMapping ("/info")
    public String openInfo(Model model, @AuthenticationPrincipal PrincipalDetails principal) {
        model.addAttribute("principal", principal);
        return "views/board/info";
    }

    @GetMapping("/list")
    public String searchBoard(Model model, @PageableDefault(size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable, String searchType, String searchKeyword, @AuthenticationPrincipal PrincipalDetails principal) {
        Page<Board> searchList = boardService.search(searchType, searchKeyword, pageable);
        model.addAttribute("boards", searchList);
        model.addAttribute("principal", principal);
        return "views/board/list";
    }

    @GetMapping("/{id}")
    public String findById(@PathVariable int id, Model model,  @AuthenticationPrincipal PrincipalDetails principal) {
        model.addAttribute("board", boardService.viewDetail(id));
        model.addAttribute("principal", principal);
        boardService.updateCount(id);
        return "views/board/detail";
    }

    @GetMapping("/{id}/update")
    public String updateForm(@PathVariable int id, Model model,  @AuthenticationPrincipal PrincipalDetails principal) {
        model.addAttribute("board", boardService.viewDetail(id));
        model.addAttribute("principal", principal);
        return "views/board/update";
    }

    @GetMapping("/write")
    public String saveForm(Model model,  @AuthenticationPrincipal PrincipalDetails principal) {
        int newId = boardService.nextId();
        model.addAttribute("principal", principal);
        model.addAttribute("id", newId);
        return "views/board/write";
    }
}
