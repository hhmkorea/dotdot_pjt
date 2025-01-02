package com.dotdot.site.controller;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@RestController
@RequestMapping("/upload/*")
public class ImageUpload {

    // 파일 업로드 경로
    final Path FILE_ROOT = Paths.get("./").toAbsolutePath().normalize();
    private String uploadPath = FILE_ROOT.toString() + "/site/upload/image/";

    @Operation(summary = "이미지 업로드", description = "이미지를 서버에 업로드한다.")
    @PostMapping("/imageUpload")
    public ResponseEntity<?> imageUpload(@RequestParam MultipartFile file) throws Exception{

        System.err.println("이미지 업로드");

        try {
            // 업로드 파일의 이름
            String originalFilename = file.getOriginalFilename();

            // 업로드 파일의 작성자
            assert originalFilename != null;
            String fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));

            // 업로드 된 파일이 중복될 수 있어서 파일 이름 재설정
            String reFileName = UUID.randomUUID().toString() + "." + fileExtension;

            // 업로드 경로에 파일명을 변경하여 저장
            file.transferTo(new File(uploadPath + reFileName));

            // 파일 이름을 재전송
            return ResponseEntity.ok(reFileName);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("업로드 에러");
        }
    }

    @Operation(summary = "이미지 삭제", description = "서버에 저장된 이미지를 삭제한다.")
    @PostMapping("/imageDelete")
    public void imageDelete(@RequestParam String file) throws Exception{
        System.err.println("이미지 삭제");
        try {
            Path path = Paths.get(uploadPath + file);
            Files.delete(path);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}

// 소스 출처 : https://greed-yb.tistory.com/267