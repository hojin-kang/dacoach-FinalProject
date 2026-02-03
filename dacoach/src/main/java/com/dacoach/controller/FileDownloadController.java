package com.dacoach.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;

import jakarta.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/files")
public class FileDownloadController {

	@Value("${file.upload.photo:/uploads/classes/photos}")
	private String photoPath;

	@Value("${file.upload.video:/uploads/classes/videos}")
	private String videoPath;

	/**
	 * 클래스 사진 다운로드/표시 URL: /files/photo/{filename}
	 */
	@GetMapping("/photo/{filename}")
	public void downloadPhoto(@PathVariable String filename, HttpServletResponse response) {
		downloadFile(filename, photoPath, response, "image");
	}

	/**
	 * 클래스 영상 다운로드/표시 URL: /files/video/{filename}
	 */
	@GetMapping("/video/{filename}")
	public void downloadVideo(@PathVariable String filename, HttpServletResponse response) {
		downloadFile(filename, videoPath, response, "video");
	}

	/**
	 * 공통 파일 다운로드 메서드
	 */
	private void downloadFile(String filename, String basePath, HttpServletResponse response, String type) {
		FileInputStream fis = null;
		OutputStream os = null;

		try {
			// 파일 경로 생성
			String filePath = basePath + File.separator + filename;
			File file = new File(filePath);

			if (!file.exists() || !file.isFile()) {
				response.sendError(HttpServletResponse.SC_NOT_FOUND, "파일을 찾을 수 없습니다.");
				return;
			}

			// 파일 확장자로 MIME 타입 설정
			String mimeType = getMimeType(filename);
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

			// 파일 전송
			fis = new FileInputStream(file);
			os = response.getOutputStream();
			FileCopyUtils.copy(fis, os);

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

	/**
	 * MIME 타입 반환
	 */
	private String getMimeType(String filename) {
		String extension = filename.substring(filename.lastIndexOf(".") + 1).toLowerCase();

		switch (extension) {
		// 이미지 타입
		case "jpg":
		case "jpeg":
			return "image/jpeg";
		case "png":
			return "image/png";
		case "gif":
			return "image/gif";
		case "bmp":
			return "image/bmp";
		case "webp":
			return "image/webp";

		// 비디오 타입
		case "mp4":
			return "video/mp4";
		case "avi":
			return "video/x-msvideo";
		case "mov":
			return "video/quicktime";
		case "wmv":
			return "video/x-ms-wmv";
		case "webm":
			return "video/webm";

		default:
			return "application/octet-stream";
		}
	}
}