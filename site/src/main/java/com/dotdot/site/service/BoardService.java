package com.dotdot.site.service;

import com.dotdot.site.controller.ImageUpload;
import com.dotdot.site.model.Board;
import com.dotdot.site.model.Member;
import com.dotdot.site.repository.BoardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class BoardService {

    // 파일 업로드 경로
    final Path FILE_ROOT = Paths.get("./").toAbsolutePath().normalize();
    private String uploadPath = FILE_ROOT.toString() + "/site/upload/image";
    private String uploadTempPath = FILE_ROOT.toString() + "/site/upload/temp";

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private ImageUpload imageUpload;

    @Transactional
    public void write(Board board, Member member) {
        board.setMember(member);
        boardRepository.save(board);
    }

    @Transactional(readOnly = true)
    public Page<Board> search(String searchType, String keyword, Pageable pageable) {
        Page<Board> boardList = null;

        if (keyword == null || keyword.trim().isEmpty()) {
            boardList = boardRepository.findAll(pageable);
        }else {
            switch (searchType) {
                case "username":
                    boardList = boardRepository.findLikeUsername(keyword, pageable);
                    break;
                case "title":
                    boardList = boardRepository.findLikeTitle(keyword, pageable);
                    break;
                case "content":
                    boardList = boardRepository.findLikeContent(keyword, pageable);
                    break;
                default:
                    boardList = boardRepository.findLikeAll(keyword, pageable);
                    break;
            }
        }
        return boardList;
    }

    @Transactional(readOnly = true)
    public int nextId() {
        return boardRepository.nextId();
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
        imageUpload.deleteFile(uploadTempPath, id);
        imageUpload.deleteFile(uploadPath, id);
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

        imageUpload.fileUpload(uploadPath, uploadTempPath, id); // image 폴더에 업로드
        imageUpload.deleteFile(uploadTempPath, id);     // temp 파일 비우기

    }

    @Transactional
    public void updateCount(int id) {
        boardRepository.updateViewCnt(id);
    }
}
