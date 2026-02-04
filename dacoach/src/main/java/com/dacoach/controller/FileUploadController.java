package com.dacoach.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.dacoach.view.FileManageView;

/**
 * 파일 업로드 컨트롤러 주로 Ajax 기반 파일 업로드에 사용
 */
@Controller
@RequestMapping("/file")
public class FileUploadController {

	@Autowired
	private FileManageView fileManageView;

	/**
	 * 이미지 파일 업로드 (다중) Ajax 전용
	 */
	@PostMapping("/upload/images")
	@ResponseBody
	public ResponseEntity<?> uploadImages(@RequestParam("images") MultipartFile[] files) {
		List<String> uploadedFiles = new ArrayList<>();

		try {
			for (MultipartFile file : files) {
				if (file.isEmpty()) {
					continue;
				}

				String savedFilename = fileManageView.savePhoto(file);
				uploadedFiles.add(savedFilename);
			}

			return ResponseEntity.ok(uploadedFiles);

		} catch (IllegalArgumentException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.internalServerError().body("파일 업로드 중 오류가 발생했습니다: " + e.getMessage());
		}
	}

	/**
	 * 동영상 파일 업로드 (단일) Ajax 전용
	 */
	@PostMapping("/upload/video")
	@ResponseBody
	public ResponseEntity<?> uploadVideo(@RequestParam("video") MultipartFile file) {
		try {
			String savedFilename = fileManageView.saveVideo(file);
			return ResponseEntity.ok(savedFilename);

		} catch (IllegalArgumentException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.internalServerError().body("파일 업로드 중 오류가 발생했습니다: " + e.getMessage());
		}
	}
}