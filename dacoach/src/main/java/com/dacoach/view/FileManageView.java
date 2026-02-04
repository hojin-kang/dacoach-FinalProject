package com.dacoach.view;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

/**
 * 파일 관리 View 헬퍼 - Thymeleaf View에서 파일 URL 생성 - 백엔드에서 파일 저장/삭제
 * 
 * 사용 예시 (Thymeleaf):
 * <img th:src="${@fileManageView.getPhotoUrl(classItem.photo)}" />
 * 
 * 사용 예시 (Service): String savedFilename = fileManageView.savePhoto(photoFile);
 */
@Component("fileManageView")
public class FileManageView {

	// ============================================
	// 설정값
	// ============================================

	@Value("${file.upload.photo:C:/uploads/classes/photos}")
	private String photoPath;

	@Value("${file.upload.video:C:/uploads/classes/videos}")
	private String videoPath;

	private static final String PHOTO_URL_PREFIX = "/files/photo/";
	private static final String VIDEO_URL_PREFIX = "/files/video/";
	private static final String DEFAULT_PHOTO = "/images/default_class.png";
	private static final String DEFAULT_VIDEO = "/images/default_video.png";

	// ============================================
	// View용 메서드 (Thymeleaf에서 사용)
	// ============================================

	public String getPhotoUrl(String filename) {
		if (filename == null || filename.trim().isEmpty()) {
			return DEFAULT_PHOTO;
		}
		return PHOTO_URL_PREFIX + filename;
	}

	public String getVideoUrl(String filename) {
		if (filename == null || filename.trim().isEmpty()) {
			return DEFAULT_VIDEO;
		}
		return VIDEO_URL_PREFIX + filename;
	}

	public boolean hasFile(String filename) {
		return filename != null && !filename.trim().isEmpty();
	}

	public String getExtension(String filename) {
		if (filename == null || !filename.contains(".")) {
			return "";
		}
		return filename.substring(filename.lastIndexOf(".") + 1).toLowerCase();
	}

	public boolean isImage(String filename) {
		String ext = getExtension(filename);
		return ext.equals("jpg") || ext.equals("jpeg") || ext.equals("png") || ext.equals("gif") || ext.equals("webp");
	}

	public boolean isVideo(String filename) {
		String ext = getExtension(filename);
		return ext.equals("mp4") || ext.equals("avi") || ext.equals("mov") || ext.equals("wmv") || ext.equals("webm");
	}

	// ============================================
	// 파일 저장 메서드 (백엔드에서 사용)
	// ============================================

	public String savePhoto(MultipartFile file) throws IOException {
		validateImageFile(file);
		return saveFile(file, photoPath);
	}

	public String saveVideo(MultipartFile file) throws IOException {
		validateVideoFile(file);
		return saveFile(file, videoPath);
	}

	/**
	 * 공통 파일 저장 로직
	 */
	private String saveFile(MultipartFile file, String targetPath) throws IOException {
		// 업로드 디렉토리 생성
		File uploadDir = new File(targetPath);
		if (!uploadDir.exists()) {
			uploadDir.mkdirs();
		}

		// 고유한 파일명 생성 (UUID + 원본 확장자)
		String originalFilename = file.getOriginalFilename();
		String extension = "";
		if (originalFilename != null && originalFilename.contains(".")) {
			extension = originalFilename.substring(originalFilename.lastIndexOf("."));
		}
		String savedFilename = UUID.randomUUID().toString() + extension;

		// 파일 저장
		Path filePath = Paths.get(targetPath, savedFilename);
		Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

		return savedFilename;
	}

	// ============================================
	// 파일 경로 및 관리 메서드
	// ============================================

	public String getPhotoPath(String filename) {
		return photoPath + File.separator + filename;
	}

	public String getVideoPath(String filename) {
		return videoPath + File.separator + filename;
	}

	public boolean fileExists(String filePath) {
		File file = new File(filePath);
		return file.exists() && file.isFile();
	}

	public boolean deleteFile(String filePath) {
		File file = new File(filePath);
		if (file.exists() && file.isFile()) {
			return file.delete();
		}
		return false;
	}

	public boolean deletePhoto(String filename) {
		return deleteFile(getPhotoPath(filename));
	}

	public boolean deleteVideo(String filename) {
		return deleteFile(getVideoPath(filename));
	}

	public String getMimeType(String filename) {
		String extension = getExtension(filename);

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

	// ============================================
	// 파일 검증 메서드
	// ============================================

	/**
	 * 이미지 파일 검증
	 */
	private void validateImageFile(MultipartFile file) {
		if (file == null || file.isEmpty()) {
			throw new IllegalArgumentException("파일을 선택해주세요.");
		}

		// 파일 타입 검증
		String contentType = file.getContentType();
		if (contentType == null || !contentType.startsWith("image/")) {
			throw new IllegalArgumentException("이미지 파일만 업로드 가능합니다.");
		}

		// 파일 크기 검증 (10MB)
		long maxSize = 10 * 1024 * 1024;
		if (file.getSize() > maxSize) {
			throw new IllegalArgumentException("이미지 파일 크기는 10MB를 초과할 수 없습니다.");
		}

		// 허용된 확장자 검증
		String filename = file.getOriginalFilename();
		if (filename != null) {
			String extension = getExtension(filename);
			if (!isAllowedImageExtension(extension)) {
				throw new IllegalArgumentException("지원하지 않는 이미지 형식입니다. (jpg, jpeg, png, gif, webp만 가능)");
			}
		}
	}

	/**
	 * 비디오 파일 검증
	 */
	private void validateVideoFile(MultipartFile file) {
		if (file == null || file.isEmpty()) {
			throw new IllegalArgumentException("파일을 선택해주세요.");
		}

		// 파일 타입 검증
		String contentType = file.getContentType();
		if (contentType == null || !contentType.startsWith("video/")) {
			throw new IllegalArgumentException("비디오 파일만 업로드 가능합니다.");
		}

		// 파일 크기 검증 (100MB)
		long maxSize = 100 * 1024 * 1024;
		if (file.getSize() > maxSize) {
			throw new IllegalArgumentException("비디오 파일 크기는 100MB를 초과할 수 없습니다.");
		}

		// 허용된 확장자 검증
		String filename = file.getOriginalFilename();
		if (filename != null) {
			String extension = getExtension(filename);
			if (!isAllowedVideoExtension(extension)) {
				throw new IllegalArgumentException("지원하지 않는 비디오 형식입니다. (mp4, avi, mov, wmv, webm만 가능)");
			}
		}
	}

	/**
	 * 허용된 이미지 확장자 확인
	 */
	private boolean isAllowedImageExtension(String extension) {
		return extension.equals("jpg") || extension.equals("jpeg") || extension.equals("png") || extension.equals("gif")
				|| extension.equals("webp");
	}

	/**
	 * 허용된 비디오 확장자 확인
	 */
	private boolean isAllowedVideoExtension(String extension) {
		return extension.equals("mp4") || extension.equals("avi") || extension.equals("mov") || extension.equals("wmv")
				|| extension.equals("webm");
	}
}