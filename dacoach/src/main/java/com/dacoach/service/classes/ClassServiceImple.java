package com.dacoach.service.classes;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Date;
import java.util.*;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.dacoach.mapper.classes.ClassMapper;
import com.dacoach.model.classes.ClassDTO;

@Service
public class ClassServiceImple implements ClassService {

	@Autowired
	private ClassMapper classMapper;

	// application.properties에서 설정값 가져오기
	@Value("${file.upload.photo:/uploads/classes/photos}")
	private String photoPath;

	@Value("${file.upload.video:/uploads/classes/videos}")
	private String videoPath;

	@Override
	@Transactional
	public int classRegister(ClassDTO classDTO) throws Exception {
		try {
			// 1. 유효성 검증
			validateClassDTO(classDTO);

			// 2. 사진 파일 업로드
			if (classDTO.getPhotoFile() != null && !classDTO.getPhotoFile().isEmpty()) {
				String savedPhotoName = uploadFile(classDTO.getPhotoFile(), photoPath);
				classDTO.setPhoto(savedPhotoName); // DB에 저장할 파일명
			}

			// 3. 영상 파일 업로드
			if (classDTO.getVideoFile() != null && !classDTO.getVideoFile().isEmpty()) {
				String savedVideoName = uploadFile(classDTO.getVideoFile(), videoPath);
				classDTO.setVideo(savedVideoName); // DB에 저장할 파일명
			}

			// 4. 클래스 정보 DB 저장
			int result = classMapper.insertClass(classDTO);

			return result;
		} catch (Exception e) {
			throw new RuntimeException("클래스 등록 중 오류가 발생했습니다: " + e.getMessage(), e);
		}
	}

	private String uploadFile(MultipartFile file, String targetPath) throws IOException {
		// 업로드 디렉토리 생성
		File uploadDir = new File(targetPath);
		if (!uploadDir.exists()) {
			uploadDir.mkdirs();
		}

		// 원본 파일명
		String originalFilename = file.getOriginalFilename();

		// 고유한 파일명 생성 (UUID 사용)
		String extension = "";
		if (originalFilename != null && originalFilename.contains(".")) {
			extension = originalFilename.substring(originalFilename.lastIndexOf("."));
		}
		String savedFilename = UUID.randomUUID().toString() + extension;

		// 파일 저장 경로
		Path filePath = Paths.get(targetPath, savedFilename);

		// 파일 저장
		Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

		return savedFilename;
	}

	private void validateClassDTO(ClassDTO classDTO) throws Exception {
		// 필수 입력 확인
		if (classDTO.getTitle() == null || classDTO.getTitle().trim().isEmpty()) {
			throw new IllegalArgumentException("제목을 입력해주세요.");
		}
		if (classDTO.getIntro() == null || classDTO.getIntro().trim().isEmpty()) {
			throw new IllegalArgumentException("내용을 입력해주세요.");
		}
		if (classDTO.getMinor_field_idx() == null) {
			throw new IllegalArgumentException("클래스 분야를 선택해주세요.");
		}
		if (classDTO.getMinor_region_idx() == null) {
			throw new IllegalArgumentException("클래스 장소를 선택해주세요.");
		}
		if (classDTO.getPrice() == null || classDTO.getPrice() < 0) {
			throw new IllegalArgumentException("올바른 가격을 입력해주세요.");
		}
		if (classDTO.getStart_date() == null) {
			throw new IllegalArgumentException("시작일을 입력해주세요.");
		}
		if (classDTO.getEnd_date() == null) {
			throw new IllegalArgumentException("종료일을 입력해주세요.");
		}
		if (classDTO.getMax_user_cnt() == null || classDTO.getMax_user_cnt() <= 0) {
			throw new IllegalArgumentException("수강 인원을 올바르게 입력해주세요.");
		}

		// 날짜 검증
		Date now = new Date();
		if (classDTO.getStart_date().before(now)) {
			throw new IllegalArgumentException("시작일은 현재 시간 이후여야 합니다.");
		}
		if (classDTO.getEnd_date().before(classDTO.getStart_date())
				|| classDTO.getEnd_date().equals(classDTO.getStart_date())) {
			throw new IllegalArgumentException("종료일은 시작일보다 이후여야 합니다.");
		}

		// 제목/내용 길이 검증
		if (classDTO.getTitle().length() > 200) {
			throw new IllegalArgumentException("제목은 200자 이내로 입력해주세요.");
		}
		if (classDTO.getIntro().length() > 3000) {
			throw new IllegalArgumentException("내용은 3000자 이내로 입력해주세요.");
		}

		// 파일 검증
		if (classDTO.getPhotoFile() != null && !classDTO.getPhotoFile().isEmpty()) {
			validateImageFile(classDTO.getPhotoFile());
		}

		if (classDTO.getVideoFile() != null && !classDTO.getVideoFile().isEmpty()) {
			validateVideoFile(classDTO.getVideoFile());
		}
	}

	private void validateImageFile(MultipartFile file) {
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
	}

	private void validateVideoFile(MultipartFile file) throws Exception {
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
	}

	@Override
	public List<Map<String, Object>> getMajorFields() throws Exception {
		return classMapper.selectMajorFields();
	}

	@Override
	public List<Map<String, Object>> getMinorFields(Integer majorFieldIdx) throws Exception {
		if (majorFieldIdx == null) {
			throw new IllegalArgumentException("대분류를 선택해주세요.");
		}
		return classMapper.selectMinorFields(majorFieldIdx);
	}

	@Override
	public List<Map<String, Object>> getMajorRegions() throws Exception {
		return classMapper.selectMajorRegions();
	}

	@Override
	public List<Map<String, Object>> getMinorRegions(Integer majorRegionIdx) throws Exception {
		if (majorRegionIdx == null) {
			throw new IllegalArgumentException("대지역을 선택해주세요.");
		}
		return classMapper.selectMinorRegions(majorRegionIdx);
	}

	@Override
	public List<ClassDTO> getClassesByProvider(Integer providerIdx) throws Exception {
		if (providerIdx == null) {
			throw new IllegalArgumentException("provider_idx가 필요합니다.");
		}
		return classMapper.selectClassesByProvider(providerIdx);
	}

	@Override
	public List<ClassDTO> searchCoachClasses(Integer minorField, Integer minorRegion, String q, String sort)
			throws Exception {
		Map<String, Object> param = new HashMap<>();
		param.put("minorField", minorField);
		param.put("minorRegion", minorRegion);
		param.put("q", (q == null ? null : q.trim()));
		param.put("sort", (sort == null ? "latest" : sort));

		return classMapper.searchCoachClasses(param);
	}

}