package com.dacoach.service.file;

import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

public class FileUpload {

    // 기본 루트 경로 설정
    private static final String ROOT_PATH = "C:/uploads/";

    public static String saveFile(MultipartFile file, String subPath) {
        if (file == null || file.isEmpty()) {
            return null;
        }

        // 1. 파일명 중복 방지를 위해 UUID 생성
        String originalFilename = file.getOriginalFilename();
        String extension = "";
        
        if (originalFilename != null && originalFilename.contains(".")) {
            extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        }
        
        String savedFilename = UUID.randomUUID().toString() + extension;

        // 2. 전체 저장 경로 생성 (C:/uploads/ + subPath)
        String fullDirectoryPath = ROOT_PATH + subPath;
        File directory = new File(fullDirectoryPath);

        // 폴더가 없으면 생성
        if (!directory.exists()) {
            directory.mkdirs();
        }

        // 3. 실제 파일 저장
        try {
        	//파일이 저장될 전체 경로 생성
            Path targetPath = Paths.get(fullDirectoryPath, savedFilename);
            //멀티파트 객체의 파일 메타데이터로 해당 전체 경로에 실제 파일 저장
            file.transferTo(targetPath);
            
            // 4. DB 저장용 경로 반환 (루트 제외한 상대 경로 + 파일명)
            // 예: profile/userprofile/uuid_filename.png
            return subPath + "/" + savedFilename;
            
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("파일 저장 중 오류가 발생했습니다.");
        }
    }
}
