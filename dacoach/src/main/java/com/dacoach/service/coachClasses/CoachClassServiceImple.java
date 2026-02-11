package com.dacoach.service.coachClasses;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dacoach.mapper.coachClasses.CoachClassMapper;
import com.dacoach.model.coachClasses.CoachClassDTO;
import com.dacoach.model.coachClasses.ClassEnrollmentDTO;

@Service
public class CoachClassServiceImple implements CoachClassService {

	@Autowired
	private CoachClassMapper classMapper;

	// ===== 기존 메서드 구현 =====

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
	public List<CoachClassDTO> classSearch(Integer majorField, Integer minorField, Integer majorRegion,
			Integer minorRegion, String q, String sort) throws Exception {
		Map<String, Object> param = new HashMap<>();
		param.put("majorField", majorField);
		param.put("minorField", minorField);
		param.put("majorRegion", majorRegion);
		param.put("minorRegion", minorRegion);
		param.put("q", (q == null ? null : q.trim()));
		param.put("sort", (sort == null ? "latest" : sort));

		return classMapper.classSearch(param);
	}

	@Override
	public CoachClassDTO getClassDetail(int class_idx) throws Exception {
		return classMapper.getClassDetail(class_idx);
	}

	@Override
	public List<String> getHashtagsByClass(int class_idx) throws Exception {
		return classMapper.selectHashtagsByClass(class_idx);
	}

	@Override
	public Map<String, Object> getClassFieldInfo(int class_idx) throws Exception {
		return classMapper.selectClassFieldInfo(class_idx);
	}

	@Override
	public Map<String, Object> getClassRegionInfo(int class_idx) throws Exception {
		return classMapper.selectClassRegionInfo(class_idx);
	}

	@Override
	public Map<String, Object> getProviderInfo(int provider_idx) throws Exception {
		return classMapper.selectProviderInfo(provider_idx);
	}

	@Override
	public List<Map<String, Object>> getReviewsByClass(int class_idx) throws Exception {
		return classMapper.selectReviewsByClass(class_idx);
	}

	@Override
	public Double getAvgRatingByClass(int class_idx) throws Exception {
		Double avgRating = classMapper.selectAvgRatingByClass(class_idx);
		return avgRating != null ? avgRating : 0.0;
	}

	@Override
	public Integer getReviewCountByClass(int class_idx) throws Exception {
		Integer count = classMapper.selectReviewCountByClass(class_idx);
		return count != null ? count : 0;
	}

	// ===== 수강신청 관련 메서드 구현 =====

	@Override
	public Integer getEnrollmentCountByDate(int class_idx, String enrollment_date) throws Exception {
		Map<String, Object> param = new HashMap<>();
		param.put("class_idx", class_idx);
		param.put("enrollment_date", enrollment_date);
		return classMapper.selectEnrollmentCountByDate(param);
	}

	@Override
	public boolean checkDuplicateEnrollment(int class_idx, int user_idx, String enrollment_date) throws Exception {
		Map<String, Object> param = new HashMap<>();
		param.put("class_idx", class_idx);
		param.put("user_idx", user_idx);
		param.put("enrollment_date", enrollment_date);

		Integer count = classMapper.checkDuplicateEnrollment(param);
		return count != null && count > 0;
	}

	@Override
	@Transactional
	public boolean enrollClass(int class_idx, int user_idx, String enrollment_date) throws Exception {
		// 1. 중복 확인
		if (checkDuplicateEnrollment(class_idx, user_idx, enrollment_date)) {
			throw new IllegalStateException("이미 해당 날짜에 수강 신청을 하셨습니다.");
		}

		// 2. 정원 확인
		CoachClassDTO classInfo = classMapper.getClassDetail(class_idx);
		Integer currentCount = getEnrollmentCountByDate(class_idx, enrollment_date);

		if (classInfo.getMax_user_cnt() != null && currentCount >= classInfo.getMax_user_cnt()) {
			throw new IllegalStateException("해당 날짜의 수강 정원이 마감되었습니다.");
		}

		// 3. 수강 신청 등록
		ClassEnrollmentDTO enrollment = new ClassEnrollmentDTO();
		enrollment.setClass_idx(class_idx);
		enrollment.setUser_idx(user_idx);
		enrollment.setStatus("CONFIRMED"); // 신청완료 상태로 등록
		enrollment.setEnrolled_at(new Date());

		int result = classMapper.insertEnrollment(enrollment);
		return result > 0;
	}

	@Override
	public List<ClassEnrollmentDTO> getEnrollmentsByUser(int user_idx) throws Exception {
		return classMapper.selectEnrollmentsByUser(user_idx);
	}

	@Override
	public List<ClassEnrollmentDTO> getEnrollmentsByClass(int class_idx) throws Exception {
		return classMapper.selectEnrollmentsByClass(class_idx);
	}

	@Override
	@Transactional
	public boolean cancelEnrollment(int enroll_idx) throws Exception {
		Map<String, Object> param = new HashMap<>();
		param.put("enroll_idx", enroll_idx);
		param.put("status", "CANCELLED");

		int result = classMapper.updateEnrollmentStatus(param);
		return result > 0;
	}

	@Override
	public ClassEnrollmentDTO getEnrollmentDetail(int enroll_idx) throws Exception {
		return classMapper.selectEnrollmentDetail(enroll_idx);
	}

	@Override
	public boolean isUserEnrolled(int class_idx, int user_idx) throws Exception {
		Map<String, Object> param = new HashMap<>();
		param.put("class_idx", class_idx);
		param.put("user_idx", user_idx);

		Integer count = classMapper.checkUserEnrollment(param);
		return count != null && count > 0;
	}
}