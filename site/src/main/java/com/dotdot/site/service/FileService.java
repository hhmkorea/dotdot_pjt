package com.dotdot.site.service;

import lombok.extern.java.Log;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.util.UUID;

@Service
@Log
public class FileService { // 파일 관련 로직

    public String uploadFile(String uploadPath, String originalFileName, byte[] fileData) throws Exception {
        UUID uuid = UUID.randomUUID();
        String extension = originalFileName.substring(originalFileName.lastIndexOf(".")); // 저장할 파일이름.
        String savedFileName = uuid.toString() + extension;
        String fileUploadFullUrl = uploadPath + "/" + savedFileName;
        FileOutputStream fos = new FileOutputStream(fileUploadFullUrl); // FileOutputStream : 바이트 단위의 출력을 내보내는 클래스, 생성자로 파일이 저장될 위치와 파일의 이름을 넘겨 파일에 쓸 파일 출력 스트림을 만듬.

        fos.write(fileData); // 파일 출력 스트림에 입력.
        fos.close();

        return savedFileName; // 업로드된 파일 이름 반환.
    }

    public void deleteFile(String filePath) throws Exception {
        File deleteFile = new File(filePath); // 파일이 저장된 경로를 이용하여 파일 객체를 생성.

        if (deleteFile.exists()) { // 해당 파일이 존재하면 삭제.
            deleteFile.delete();
            log.info("파일을 삭제하였습니다.");
        } else {
            log.info("파일이 존재하지 않습니다.");
        }
    }
}
