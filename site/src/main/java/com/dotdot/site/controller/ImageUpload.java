package com.dotdot.site.controller;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
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

    // summernote 에디터 이미지 추가 버튼 : /upload/image/{id}
    @Operation(summary = "이미지 업로드", description = "이미지를 서버에 업로드한다.")
    @PostMapping("/imageUpload/{id}")
    public ResponseEntity<?> imageUpload(@RequestParam MultipartFile file, @PathVariable Integer id) throws Exception{

        System.err.println("이미지 업로드");

        // 주어진 경로를 기반으로 게시물 번호 객체 생성
        File folder1;
        File folder2;
        folder1 = new File(uploadPath + "/" + id);
        folder2 = new File(uploadTempPath + "/" + id);

        // 폴더가 존재하지 않으면 새로 생성
        if (!folder1.exists())
            folder1.mkdirs();
        if (!folder2.exists())
            folder2.mkdirs();

        try {
            // 업로드 파일의 이름
            String originalFilename = file.getOriginalFilename();

            // 업로드 파일의 작성자
            assert originalFilename != null;
            String fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));

            // 업로드 된 파일이 중복될 수 있어서 파일 이름 재설정
            String reFileName = UUID.randomUUID().toString() + "." + fileExtension;

            // 업로드 경로에 파일명을 변경하여 저장
            file.transferTo(new File(uploadPath +"/"+ id + "/"+ reFileName));
            deleteFile(uploadTempPath, id);     // temp 폴더 비우기
            // 파일 이름을 재전송
            return ResponseEntity.ok(reFileName);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("업로드 에러");
        }
    }

    // summernote 에디터 이미지 삭제 버튼 : /upload/image/{id}
    @Operation(summary = "이미지 삭제", description = "서버에 저장된 이미지를 삭제한다.")
    @PostMapping("/imageDelete/{id}")
    public void imageDelete(@RequestParam String file, @PathVariable Integer id) throws Exception{
        System.err.println("이미지 삭제"); // 미리보기 안에서만 삭제. 실제 삭제는 게시물 삭제할 경우 삭제됨. 여기서 실제로 삭제하면 게시물 수정 취소할 경우 이미지 깨져서 않보이게 됨.
//        try {
//            Path path = Paths.get(uploadPath + "/" + id + "/" + file);  // image 폴더 이미지 파일 지우기
//            Files.delete(path);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }

    // 이미지 업로드(to Server) : /upload/image/{id}, /upload/temp/{id}, temp 폴더에 백업 후 image 폴더에 업로드
    public void fileUpload(String uploadPath, String uploadTempPath, int id) {
        // 주어진 경로를 기반으로 폴더 객체 생성
        File folder1;
        File folder2;
        uploadPath = uploadPath + "/" + id;
        uploadTempPath = uploadTempPath + "/" + id;
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

    // 이미지 삭제(from Server) : /upload/image/{id}, /upload/temp/{id}, {id} 폴더 포함 이미지 파일 삭제.
    public void deleteFile(String path, int id) {
        path = path + "/" + id;
        File file = new File(path);
        try {
            if (file.exists()) {
                File[] file_list = file.listFiles();
                for (int i = 0; i < file_list.length; i++) {
                    file_list[i].delete();      // 게시물 번호 폴더에 들어있는 이미지 파일 삭제
                }
                file.delete();                  // 게시물 번호 폴더 삭제
            }
        } catch (Exception e) {
            e.getStackTrace();
        }
    }

}

// 소스 출처 : https://greed-yb.tistory.com/267