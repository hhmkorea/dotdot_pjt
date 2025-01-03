package com.dotdot.site.controller;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@RestController
@RequestMapping("/upload/*")
public class ImageUpload {

    // 파일 업로드 경로
    final Path FILE_ROOT = Paths.get("./").toAbsolutePath().normalize();
    private String uploadPath = FILE_ROOT.toString() + "/site/upload/image";
    private String uploadTempPath = FILE_ROOT.toString() + "/site/upload/temp";

    @Operation(summary = "이미지 업로드", description = "이미지를 서버에 업로드한다.")
    @PostMapping("/imageUpload")
    public ResponseEntity<?> imageUpload(@RequestParam MultipartFile file) throws Exception{

        // 주어진 경로를 기반으로 폴더 객체 생성
        File folder1;
        File folder2;
        folder1 = new File(uploadPath);
        folder2 = new File(uploadTempPath);

        // 폴더가 존재하지 않으면 새로 생성
        if (!folder1.exists())
            folder1.mkdirs();
        if (!folder2.exists())
            folder2.mkdirs();

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
            file.transferTo(new File(uploadPath + "/"+ reFileName));

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
            Path path = Paths.get(uploadTempPath + "/" + file);
            Files.delete(path);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 하위 폴더 복사
    public void fileUpload(String uploadPath, String uploadTempPath) {
        // 주어진 경로를 기반으로 폴더 객체 생성
        File folder1;
        File folder2;
        folder1 = new File(uploadPath);
        folder2 = new File(uploadTempPath);

        // 폴더가 존재하지 않으면 새로 생성
        if (!folder1.exists())
            folder1.mkdirs();
        if (!folder2.exists())
            folder2.mkdirs();

        // 폴더1의 파일들을 가져옴
        File[] target_files = folder1.listFiles();
        for (File file : target_files) {
            // 폴더2에 동일한 파일 생성
            File temp = new File(folder2.getAbsolutePath() + File.separator + file.getName());

            // 파일인 경우
            if (file.isDirectory()) {
                // 폴더인 경우 동일한 폴더 생성
                temp.mkdir();
            } else {
                // 폴더가 아닌 경우 파일 복사
                FileInputStream fis = null;
                FileOutputStream fos = null;
                try {
                    fis = new FileInputStream(file);
                    fos = new FileOutputStream(temp);
                    byte[] b = new byte[4096];
                    int cnt = 0;
                    while ((cnt = fis.read(b)) != -1) {
                        fos.write(b, 0, cnt);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    try {
                        // 예외 발생 여부와 상관없이 파일 입출력 스트림을 닫음
                        fis.close();
                        fos.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    // 하위 폴더 삭제
    public void deleteFile(String path) {
        // 주어진 경로에 있는 폴더와 파일을 재귀적으로 삭제하는 함수입니다.

        File file = new File(path);
        try {
            if (file.exists()) {
                File[] file_list = file.listFiles();
                for (int i = 0; i < file_list.length; i++) {
                    file_list[i].delete();
                }
            }
        } catch (Exception e) {
            e.getStackTrace();
        }
    }

}

// 소스 출처 : https://greed-yb.tistory.com/267