package com.dacoach.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

/**
 * 파일 업로드 및 제공 통합 컨트롤러 - 파일 업로드 (사진, 비디오, 프로필) - 파일 제공 (웹에서 접근) - 파일 삭제
 */
@Controller
public class FileUploadController {

	// ============================================
	// 설정값
	// ============================================

	@Value("${file.upload.photo:C:/uploads/classes/photos}")
	private String photoPath;

	@Value("${file.upload.video:C:/uploads/classes/videos}")
	private String videoPath;

	@Value("${file.upload.profile:C:/uploads/profile}")
	private String profilePath;

	private static final long MAX_IMAGE_SIZE = 10 * 1024 * 1024; // 10MB
	private static final long MAX_VIDEO_SIZE = 100 * 1024 * 1024; // 100MB

	// ============================================
	// 파일 제공 (GET) - 웹에서 파일 접근
	// ============================================

	/**
	 * 클래스 사진 파일 제공 GET /files/photo/{filename}
	 */
	@GetMapping("/files/photo/{filename:.+}")
	@ResponseBody
	public ResponseEntity<Resource> getPhoto(@PathVariable String filename) {
		return serveFile(photoPath, filename);
	}

	/**
	 * 클래스 비디오 파일 제공 GET /files/video/{filename}
	 */
	@GetMapping("/files/video/{filename:.+}")
	@ResponseBody
	public ResponseEntity<Resource> getVideo(@PathVariable String filename) {
		return serveFile(videoPath, filename);
	}

	/**
	 * 프로필 이미지 파일 제공 GET /files/profile/{filename}
	 */
	@GetMapping("/files/profile/{filename:.+}")
	@ResponseBody
	public ResponseEntity<Resource> getProfile(@PathVariable String filename) {
		return serveFile(profilePath, filename);
	}

	/**
	 * 파일 제공 공통 로직
	 */
	private ResponseEntity<Resource> serveFile(String basePath, String filename) {
		try {
			// 경로 조작 방지를 위한 파일명 검증
			if (filename.contains("..") || filename.contains("/") || filename.contains("\\")) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
			}

			Path filePath = Paths.get(basePath, filename);
			File file = filePath.toFile();

			// 파일 존재 여부 확인
			if (!file.exists() || !file.isFile()) {
				return ResponseEntity.notFound().build();
			}

			Resource resource = new FileSystemResource(file);

			// Content-Type 결정
			String contentType = getMimeType(filename);

			return ResponseEntity.ok().contentType(MediaType.parseMediaType(contentType))
					.header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + filename + "\"").body(resource);

		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	// ============================================
	// 파일 업로드 API (POST)
	// ============================================

	/**
	 * 이미지 파일 업로드 (다중) POST /file/upload/images Ajax 전용
	 */
	@PostMapping("/file/upload/images")
	@ResponseBody
	public ResponseEntity<?> uploadImages(@RequestParam("images") MultipartFile[] files) {
		List<String> uploadedFiles = new ArrayList<>();

		try {
			for (MultipartFile file : files) {
				if (file.isEmpty()) {
					continue;
				}

				validateImageFile(file);
				String savedFilename = saveFile(file, photoPath);
				uploadedFiles.add(savedFilename);
			}

			Map<String, Object> response = new HashMap<>();
			response.put("success", true);
			response.put("files", uploadedFiles);
			return ResponseEntity.ok(response);

		} catch (IllegalArgumentException e) {
			return ResponseEntity.badRequest().body(createErrorResponse(e.getMessage()));
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.internalServerError()
					.body(createErrorResponse("파일 업로드 중 오류가 발생했습니다: " + e.getMessage()));
		}
	}

	/**
	 * 동영상 파일 업로드 (단일) POST /file/upload/video Ajax 전용
	 */
	@PostMapping("/file/upload/video")
	@ResponseBody
	public ResponseEntity<?> uploadVideo(@RequestParam("video") MultipartFile file) {
		try {
			validateVideoFile(file);
			String savedFilename = saveFile(file, videoPath);

			Map<String, Object> response = new HashMap<>();
			response.put("success", true);
			response.put("filename", savedFilename);
			return ResponseEntity.ok(response);

		} catch (IllegalArgumentException e) {
			return ResponseEntity.badRequest().body(createErrorResponse(e.getMessage()));
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.internalServerError()
					.body(createErrorResponse("파일 업로드 중 오류가 발생했습니다: " + e.getMessage()));
		}
	}

	/**
	 * 프로필 이미지 업로드 (단일) POST /file/upload/profile
	 */
	@PostMapping("/file/upload/profile")
	@ResponseBody
	public ResponseEntity<?> uploadProfile(@RequestParam("profile") MultipartFile file) {
		try {
			validateImageFile(file);
			String savedFilename = saveFile(file, profilePath);

			Map<String, Object> response = new HashMap<>();
			response.put("success", true);
			response.put("filename", savedFilename);
			return ResponseEntity.ok(response);

		} catch (IllegalArgumentException e) {
			return ResponseEntity.badRequest().body(createErrorResponse(e.getMessage()));
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.internalServerError()
					.body(createErrorResponse("파일 업로드 중 오류가 발생했습니다: " + e.getMessage()));
		}
	}

	// ============================================
	// 서비스 레이어에서 호출하는 파일 저장 메서드
	// ============================================

	/**
	 * 사진 파일 저장 (서비스 레이어에서 호출용)
	 */
	public String savePhoto(MultipartFile file) throws IOException {
		validateImageFile(file);
		return saveFile(file, photoPath);
	}

	/**
	 * 비디오 파일 저장 (서비스 레이어에서 호출용)
	 */
	public String saveVideo(MultipartFile file) throws IOException {
		validateVideoFile(file);
		return saveFile(file, videoPath);
	}

	/**
	 * 프로필 이미지 저장 (서비스 레이어에서 호출용)
	 */
	public String saveProfile(MultipartFile file) throws IOException {
		validateImageFile(file);
		return saveFile(file, profilePath);
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
	// 파일 삭제 로직
	// ============================================

	/**
	 * 사진 파일 삭제
	 */
	public boolean deletePhoto(String filename) {
		return deleteFile(getPhotoPath(filename));
	}

	/**
	 * 비디오 파일 삭제
	 */
	public boolean deleteVideo(String filename) {
		return deleteFile(getVideoPath(filename));
	}

	/**
	 * 프로필 이미지 삭제
	 */
	public boolean deleteProfile(String filename) {
		return deleteFile(getProfilePath(filename));
	}

	/**
	 * 파일 삭제 공통 로직
	 */
	private boolean deleteFile(String filePath) {
		File file = new File(filePath);
		if (file.exists() && file.isFile()) {
			return file.delete();
		}
		return false;
	}

	// ============================================
	// 파일 경로 관리
	// ============================================

	public String getPhotoPath(String filename) {
		return photoPath + File.separator + filename;
	}

	public String getVideoPath(String filename) {
		return videoPath + File.separator + filename;
	}

	public String getProfilePath(String filename) {
		return profilePath + File.separator + filename;
	}

	public boolean fileExists(String filePath) {
		File file = new File(filePath);
		return file.exists() && file.isFile();
	}

	// ============================================
	// URL 생성 (Thymeleaf 또는 서비스에서 사용)
	// ============================================

	private static final String PHOTO_URL_PREFIX = "/files/photo/";
	private static final String VIDEO_URL_PREFIX = "/files/video/";
	private static final String PROFILE_URL_PREFIX = "/files/profile/";
	private static final String DEFAULT_PHOTO = "/images/default_class.png";
	private static final String DEFAULT_VIDEO = "/images/default_video.png";
	private static final String DEFAULT_PROFILE = "/images/default_profile.png";

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

	public String getProfileUrl(String filename) {
		if (filename == null || filename.trim().isEmpty()) {
			return DEFAULT_PROFILE;
		}
		return PROFILE_URL_PREFIX + filename;
	}

	// ============================================
	// 파일 검증
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

		// 파일 크기 검증
		if (file.getSize() > MAX_IMAGE_SIZE) {
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

		// 파일 크기 검증
		if (file.getSize() > MAX_VIDEO_SIZE) {
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

	// ============================================
	// 유틸리티 메서드
	// ============================================

	private String getExtension(String filename) {
		if (filename == null || !filename.contains(".")) {
			return "";
		}
		return filename.substring(filename.lastIndexOf(".") + 1).toLowerCase();
	}

	private boolean isAllowedImageExtension(String extension) {
		return extension.equals("jpg") || extension.equals("jpeg") || extension.equals("png") || extension.equals("gif")
				|| extension.equals("webp");
	}

	private boolean isAllowedVideoExtension(String extension) {
		return extension.equals("mp4") || extension.equals("avi") || extension.equals("mov") || extension.equals("wmv")
				|| extension.equals("webm");
	}

	public boolean isImage(String filename) {
		String ext = getExtension(filename);
		return isAllowedImageExtension(ext);
	}

	public boolean isVideo(String filename) {
		String ext = getExtension(filename);
		return isAllowedVideoExtension(ext);
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

	private Map<String, Object> createErrorResponse(String message) {
		Map<String, Object> response = new HashMap<>();
		response.put("success", false);
		response.put("error", message);
		return response;
	}
}