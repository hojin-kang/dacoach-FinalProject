package com.dacoach.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;

import jakarta.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.dacoach.view.FileManageView;

@Controller
@RequestMapping("/files")
public class FileDownloadController {

	@Autowired
	private FileManageView fileManageView;

	/**
	 * 클래스 사진 다운로드/표시 URL: /files/photo/{filename}
	 */
	@GetMapping("/photo/{filename}")
	public void downloadPhoto(@PathVariable String filename, HttpServletResponse response) {
		String filePath = fileManageView.getPhotoPath(filename);
		downloadFile(filePath, filename, response, "image");
	}

	/**
	 * 클래스 영상 다운로드/표시 URL: /files/video/{filename}
	 */
	@GetMapping("/video/{filename}")
	public void downloadVideo(@PathVariable String filename, HttpServletResponse response) {
		String filePath = fileManageView.getVideoPath(filename);
		downloadFile(filePath, filename, response, "video");
	}

	/**
	 * 공통 파일 다운로드 메서드
	 */
	private void downloadFile(String filePath, String filename, HttpServletResponse response, String type) {
		FileInputStream fis = null;
		OutputStream os = null;

		try {
			File file = new File(filePath);

			// 파일 존재 확인
			if (!fileManageView.fileExists(filePath)) {
				response.sendError(HttpServletResponse.SC_NOT_FOUND, "파일을 찾을 수 없습니다.");
				return;
			}

			// MIME 타입 설정
			String mimeType = fileManageView.getMimeType(filename);
			response.setContentType(mimeType);

			// 파일 크기 설정
			response.setContentLengthLong(file.length());

			// 파일명 인코딩 (한글 파일명 지원)
			String encodedFilename = URLEncoder.encode(filename, "UTF-8").replaceAll("\\+", "%20");

			// Content-Disposition 헤더 설정
			if ("video".equals(type)) {
				// 비디오는 스트리밍을 위해 inline + Accept-Ranges 추가
				response.setHeader("Content-Disposition", "inline; filename=\"" + encodedFilename + "\"");
				response.setHeader("Accept-Ranges", "bytes");
			} else {
				// 이미지는 브라우저에서 바로 표시
				response.setHeader("Content-Disposition", "inline; filename=\"" + encodedFilename + "\"");
			}

			// 캐시 헤더 추가 (성능 향상)
			response.setHeader("Cache-Control", "public, max-age=31536000");

			// 파일 전송
			fis = new FileInputStream(file);
			os = response.getOutputStream();
			FileCopyUtils.copy(fis, os);
			os.flush();

		} catch (IOException e) {
			e.printStackTrace();
			try {
				response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "파일 다운로드 중 오류가 발생했습니다.");
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		} finally {
			try {
				if (fis != null)
					fis.close();
				if (os != null)
					os.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}