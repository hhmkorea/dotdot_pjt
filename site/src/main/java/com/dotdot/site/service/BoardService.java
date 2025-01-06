package com.dotdot.site.service;

import com.dotdot.site.controller.ImageUpload;
import com.dotdot.site.model.Board;
import com.dotdot.site.model.Member;
import com.dotdot.site.repository.BoardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
        Sort sortDescName = Sort.by(Sort.Direction.DESC, "id");
        Pageable pageRequest = PageRequest.of(pageable.getPageNumber(), 10, sortDescName);

        Page<Board> boardList = null;

        if (keyword == null || keyword.trim().isEmpty()) {
            boardList = boardRepository.findAll(pageRequest);
        }else {
            boardList = switch (searchType) {
                case "username" -> boardRepository.findLikeUsername(keyword, pageable);
                case "title" -> boardRepository.findLikeTitle(keyword, pageable);
                case "content" -> boardRepository.findLikeContent(keyword, pageable);
                default -> boardRepository.findLikeAll(keyword, pageable);
            };
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
        imageUpload.deleteFile(uploadTempPath, id);     // temp 폴더 비우기
        imageUpload.deleteFile(uploadPath, id);         // image 폴더 비우기
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

        imageUpload.fileUpload(uploadPath, uploadTempPath, id); // 이전 이미지 temp 폴더에 복사해 두고 신규 이미지와 함께 image 폴더에 이미지 업로드
        imageUpload.deleteFile(uploadTempPath, id);     // temp 폴더 비우기

    }

    @Transactional
    public void updateCount(int id) {
        boardRepository.updateViewCnt(id);
    }
}
