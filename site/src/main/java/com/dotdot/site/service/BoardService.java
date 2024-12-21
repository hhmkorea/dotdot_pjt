package com.dotdot.site.service;

import com.dotdot.site.model.Board;
import com.dotdot.site.repository.BoardRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BoardService {

    @Autowired
    private BoardRepository boardRepository;

    @Transactional
    public void write(Board board) {
        board.setViewcnt(0);
        board.setUserName("tester");
        boardRepository.save(board);
    }

    @Transactional
    public Board viewDetail(int id) {
        return boardRepository.findById(id).orElseThrow(() -> {
                return new IllegalStateException("글 상세보기 실패 : 선택된 게시물을 찾을 수 없습니다.");
            }
        );
    }
    
    @Transactional
    public void deleteById(int id) {
        System.out.println("deleteById : " + id);
        boardRepository.deleteById(id);
    }

    @Transactional
    public void update(int id, Board requestBoard) {
        Board board = boardRepository.findById(id).orElseThrow(() -> {
                return new IllegalArgumentException("글 찾기 실패 : 선택된 게시물을 찾을 수 없습니다.");
            }
        );
        board.setTitle(requestBoard.getTitle());
        board.setContent(requestBoard.getContent());
    }
}
