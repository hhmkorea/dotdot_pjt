package com.dotdot.site.service;

import com.dotdot.site.model.Board;
import com.dotdot.site.model.Member;
import com.dotdot.site.repository.BoardRepository;
import com.dotdot.site.repository.BoardSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BoardService {

    @Autowired
    private BoardRepository boardRepository;

    @Transactional
    public void write(Board board, Member member) {
        board.setMember(member);
        boardRepository.save(board);
    }

    @Transactional(readOnly = true)
    public Page<Board> search(String searchType, String keyword, Pageable pageable) {
        Page<Board> boardList = null;

        Specification<Board> spec = (root, query, criteriBuilder) -> null;

        if (keyword == null || keyword.trim().isEmpty()) {
            boardList = boardRepository.findAll(pageable);
        } else {
            switch (searchType) {
//                case "username":
//                    spec = spec.and(BoardSpecification.likeUsername(keyword));
//                    break;
                case "title":
                    spec = spec.and(BoardSpecification.likeTitle(keyword));
                    break;
                case "content":
                    spec = spec.and(BoardSpecification.likeContent(keyword));
                    break;
                default:
                    //spec = spec.or(BoardSpecification.likeUsername(keyword)).or(BoardSpecification.likeTitle(keyword)).or(BoardSpecification.likeContent(keyword));
                    spec = spec.or(BoardSpecification.likeTitle(keyword)).or(BoardSpecification.likeContent(keyword));
                    break;
            }
            boardList = boardRepository.findAll(spec, pageable);
        }
        return boardList;
    }

    @Transactional(readOnly = true)
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

    @Transactional
    public void updateCount(int id) {
        boardRepository.updateViewCnt(id);
    }
}
