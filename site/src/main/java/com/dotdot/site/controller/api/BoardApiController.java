package com.dotdot.site.controller.api;

import com.dotdot.site.controller.dto.BoardResponseDto;
import com.dotdot.site.model.Board;
import com.dotdot.site.model.Member;
import com.dotdot.site.service.BoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController // JSON 데이타 리턴
public class BoardApiController {

    @Autowired
    private BoardService boardService;

    @PostMapping("/api/board")
    public BoardResponseDto<Integer> save(@RequestBody Board board) {
        Member member = new Member();
        member.setId(0);
        member.setUserName("tester");
        boardService.write(board);
        return new BoardResponseDto<Integer>(HttpStatus.OK.value(), 1);
    }

    @DeleteMapping("/api/board/{id}")
    public BoardResponseDto<Integer> deleteById(@PathVariable Integer id) {
        boardService.deleteById(id);
        return new BoardResponseDto<Integer>(HttpStatus.OK.value(), 1);
    }

    @PutMapping("/api/board/{id}")
    public BoardResponseDto<Integer> update(@PathVariable Integer id, @RequestBody Board board) {
        boardService.update(id, board);
        return new BoardResponseDto<Integer>(HttpStatus.OK.value(), 1);
    }
}
