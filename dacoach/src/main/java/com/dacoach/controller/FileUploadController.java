package com.dacoach.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/file")
public class FileUploadController {

	// application.properties에서 설정 가능
	// file.upload-dir=C:/uploads/class
	@Value("${file.upload-dir:./uploads/class}")
	private String uploadDir;

	/**
	 * 이미지 파일 업로드
	 */
	@PostMapping("/upload/images")
	@ResponseBody
	public ResponseEntity<?> uploadImages(@RequestParam("images") MultipartFile[] files) {

		List<String> uploadedFiles = new ArrayList<>();

		try {
			// 업로드 디렉토리 생성
			File directory = new File(uploadDir);
			if (!directory.exists()) {
				directory.mkdirs();
			}

			for (MultipartFile file : files) {
				// 파일이 비어있는지 확인
				if (file.isEmpty()) {
					continue;
				}

				// 파일 타입 검증
				String contentType = file.getContentType();
				if (contentType == null || !contentType.startsWith("image/")) {
					return ResponseEntity.badRequest().body("이미지 파일만 업로드 가능합니다: " + file.getOriginalFilename());
				}

				// 파일 크기 검증 (5MB)
				if (file.getSize() > 5 * 1024 * 1024) {
					return ResponseEntity.badRequest().body("파일 크기는 5MB 이하여야 합니다: " + file.getOriginalFilename());
				}

				// 고유한 파일명 생성
				String originalFilename = file.getOriginalFilename();
				String extension = "";
				if (originalFilename != null && originalFilename.contains(".")) {
					extension = originalFilename.substring(originalFilename.lastIndexOf("."));
				}
				String uniqueFilename = UUID.randomUUID().toString() + extension;

				// 파일 저장
				Path filePath = Paths.get(uploadDir, uniqueFilename);
				Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

				uploadedFiles.add(uniqueFilename);
			}

			return ResponseEntity.ok(uploadedFiles);

		} catch (IOException e) {
			e.printStackTrace();
			return ResponseEntity.internalServerError().body("파일 업로드 중 오류가 발생했습니다: " + e.getMessage());
		}
	}

	/**
	 * 동영상 파일 업로드
	 */
	@PostMapping("/upload/video")
	@ResponseBody
	public ResponseEntity<?> uploadVideo(@RequestParam("video") MultipartFile file) {

		try {
			// 파일이 비어있는지 확인
			if (file.isEmpty()) {
				return ResponseEntity.badRequest().body("파일을 선택해주세요.");
			}

			// 파일 타입 검증
			String contentType = file.getContentType();
			if (contentType == null || !contentType.startsWith("video/")) {
				return ResponseEntity.badRequest().body("동영상 파일만 업로드 가능합니다.");
			}

			// 파일 크기 검증 (100MB)
			if (file.getSize() > 100 * 1024 * 1024) {
				return ResponseEntity.badRequest().body("동영상 파일 크기는 100MB 이하여야 합니다.");
			}

			// 업로드 디렉토리 생성
			File directory = new File(uploadDir);
			if (!directory.exists()) {
				directory.mkdirs();
			}

			// 고유한 파일명 생성
			String originalFilename = file.getOriginalFilename();
			String extension = "";
			if (originalFilename != null && originalFilename.contains(".")) {
				extension = originalFilename.substring(originalFilename.lastIndexOf("."));
			}
			String uniqueFilename = UUID.randomUUID().toString() + extension;

			// 파일 저장
			Path filePath = Paths.get(uploadDir, uniqueFilename);
			Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

			return ResponseEntity.ok(uniqueFilename);

		} catch (IOException e) {
			e.printStackTrace();
			return ResponseEntity.internalServerError().body("파일 업로드 중 오류가 발생했습니다: " + e.getMessage());
		}
	}
}