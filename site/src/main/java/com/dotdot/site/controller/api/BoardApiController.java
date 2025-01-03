package com.dotdot.site.controller.api;

import com.dotdot.site.config.auth.PrincipalDetails;
import com.dotdot.site.controller.dto.BoardResponseDto;
import com.dotdot.site.model.Board;
import com.dotdot.site.service.BoardService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController // JSON 데이타 리턴
@RequestMapping("/api")
public class BoardApiController {

    @Autowired
    private BoardService boardService;

    @PostMapping("/board")
    public BoardResponseDto<Integer> save(@Valid @RequestBody Board board, @AuthenticationPrincipal PrincipalDetails principal) {
        boardService.write(board, principal.getMember());
        return new BoardResponseDto<Integer>(HttpStatus.OK.value(), 1);
    }

    @DeleteMapping("/board/{id}")
    public BoardResponseDto<Integer> deleteById(@PathVariable Integer id) {
        boardService.deleteById(id);
        return new BoardResponseDto<Integer>(HttpStatus.OK.value(), 1);
    }

    @PutMapping("/board/{id}")
    public BoardResponseDto<Integer> update(@Valid @RequestBody Board board, @PathVariable Integer id) {
        boardService.update(id, board);
        return new BoardResponseDto<Integer>(HttpStatus.OK.value(), 1);
    }
}
