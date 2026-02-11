package com.dacoach.service.coachClasses;

import java.util.List;
import java.util.Map;

import com.dacoach.model.coachClasses.CoachClassDTO;
import com.dacoach.model.coachClasses.ClassEnrollmentDTO;

public interface CoachClassService {

	// ===== 기존 메서드 =====

	List<Map<String, Object>> getMajorFields() throws Exception;

	List<Map<String, Object>> getMinorFields(Integer majorFieldIdx) throws Exception;

	List<Map<String, Object>> getMajorRegions() throws Exception;

	List<Map<String, Object>> getMinorRegions(Integer majorRegionIdx) throws Exception;

	List<CoachClassDTO> classSearch(Integer majorField, Integer minorField, Integer majorRegion, Integer minorRegion,
			String q, String sort) throws Exception;

	CoachClassDTO getClassDetail(int class_idx) throws Exception;

	List<String> getHashtagsByClass(int class_idx) throws Exception;

	Map<String, Object> getClassFieldInfo(int class_idx) throws Exception;

	Map<String, Object> getClassRegionInfo(int class_idx) throws Exception;

	Map<String, Object> getProviderInfo(int provider_idx) throws Exception;

	List<Map<String, Object>> getReviewsByClass(int class_idx) throws Exception;

	Double getAvgRatingByClass(int class_idx) throws Exception;

	Integer getReviewCountByClass(int class_idx) throws Exception;

	// ===== 수강신청 관련 메서드 추가 =====

	/**
	 * 특정 날짜의 수강 신청 인원 수 조회
	 */
	Integer getEnrollmentCountByDate(int class_idx, String enrollment_date) throws Exception;

	/**
	 * 수강 신청 중복 확인
	 */
	boolean checkDuplicateEnrollment(int class_idx, int user_idx, String enrollment_date) throws Exception;

	/**
	 * 수강 신청 등록
	 */
	boolean enrollClass(int class_idx, int user_idx, String enrollment_date) throws Exception;

	/**
	 * 특정 유저의 수강 신청 목록 조회
	 */
	List<ClassEnrollmentDTO> getEnrollmentsByUser(int user_idx) throws Exception;

	/**
	 * 특정 클래스의 수강 신청 목록 조회
	 */
	List<ClassEnrollmentDTO> getEnrollmentsByClass(int class_idx) throws Exception;

	/**
	 * 수강 신청 취소
	 */
	boolean cancelEnrollment(int enroll_idx) throws Exception;

	/**
	 * 수강 신청 상세 조회
	 */
	ClassEnrollmentDTO getEnrollmentDetail(int enroll_idx) throws Exception;

	/**
	 * 사용자가 해당 클래스를 이미 신청했는지 확인
	 */
	boolean isUserEnrolled(int class_idx, int user_idx) throws Exception;
}