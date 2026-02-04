package com.dacoach.service.classes;

import java.util.Date;
import java.util.*;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dacoach.mapper.classes.ClassMapper;
import com.dacoach.model.classes.ClassDTO;
import com.dacoach.view.FileManageView;

@Service
public class ClassServiceImple implements ClassService {

	@Autowired
	private ClassMapper classMapper;

	@Autowired
	private FileManageView fileManageView;

	@Override
	@Transactional
	public int classRegister(ClassDTO classDTO) throws Exception {
		try {
			// 1. 유효성 검증
			validateClassDTO(classDTO);

			// 2. 사진 파일 업로드
			if (classDTO.getPhotoFile() != null && !classDTO.getPhotoFile().isEmpty()) {
				String savedPhotoName = fileManageView.savePhoto(classDTO.getPhotoFile());
				classDTO.setPhoto(savedPhotoName);
			}

			// 3. 영상 파일 업로드
			if (classDTO.getVideoFile() != null && !classDTO.getVideoFile().isEmpty()) {
				String savedVideoName = fileManageView.saveVideo(classDTO.getVideoFile());
				classDTO.setVideo(savedVideoName);
			}

			// 4. 클래스 정보 DB 저장
			int result = classMapper.insertClass(classDTO);

			return result;
		} catch (IllegalArgumentException e) {
			// 파일 검증 실패 등
			throw e;
		} catch (Exception e) {
			throw new RuntimeException("클래스 등록 중 오류가 발생했습니다: " + e.getMessage(), e);
		}
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

		// 파일 검증은 FileManageView에서 수행
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
	public List<ClassDTO> classSearch(Integer majorField, Integer minorField, Integer majorRegion, Integer minorRegion,
			String q, String sort) throws Exception {
		Map<String, Object> param = new HashMap<>();
		param.put("majorField", majorField);
		param.put("minorField", minorField);
		param.put("majorRegion", majorRegion);
		param.put("minorRegion", minorRegion);
		param.put("q", (q == null ? null : q.trim()));
		param.put("sort", (sort == null ? "latest" : sort));

		return classMapper.classSearch(param);
	}

}