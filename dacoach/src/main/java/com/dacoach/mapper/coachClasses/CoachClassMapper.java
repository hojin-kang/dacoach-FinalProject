package com.dacoach.mapper.coachClasses;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.dacoach.model.coachClasses.CoachClassDTO;
import com.dacoach.model.coachClasses.ClassEnrollmentDTO;
import com.dacoach.model.review.ReviewClassDTO;

@Mapper
public interface CoachClassMapper {

	// ===== 기존 메서드 =====

	// 대분류 조회 (Ajax용)
	List<Map<String, Object>> selectMajorFields() throws Exception;

	// 소분류 조회 (대분류 선택시 Ajax)
	List<Map<String, Object>> selectMinorFields(Integer major_field_idx) throws Exception;

	// 대지역 조회 (Ajax용)
	List<Map<String, Object>> selectMajorRegions() throws Exception;

	// 소지역 조회 (대지역 선택시 Ajax)
	List<Map<String, Object>> selectMinorRegions(Integer major_region_idx) throws Exception;

	// 코치 - 클래스 검색
	List<CoachClassDTO> classSearch(Map<String, Object> param) throws Exception;

	// 코치 - 클래스 상세
	CoachClassDTO getClassDetail(int class_idx) throws Exception;

	// 해시태그 목록 조회
	List<String> selectHashtagsByClass(int class_idx) throws Exception;

	// 분야 정보 조회 (대분류, 소분류)
	Map<String, Object> selectClassFieldInfo(int class_idx) throws Exception;

	// 지역 정보 조회 (대지역, 소지역)
	Map<String, Object> selectClassRegionInfo(int class_idx) throws Exception;

	// 제공자 정보 조회 (이름, 사진)
	Map<String, Object> selectProviderInfo(int provider_idx) throws Exception;

	// 리뷰 목록 조회
	List<Map<String, Object>> selectReviewsByClass(int class_idx) throws Exception;

	// 평균 평점 조회
	Double selectAvgRatingByClass(int class_idx) throws Exception;

	// 리뷰 개수 조회
	Integer selectReviewCountByClass(int class_idx) throws Exception;

	// ===== 수강신청 관련 메서드 =====

	// 특정 날짜의 수강 신청 인원 수 조회
	Integer selectEnrollmentCountByDate(Map<String, Object> param) throws Exception;

	// 수강 신청 중복 확인 (같은 날짜에 이미 신청했는지)
	Integer checkDuplicateEnrollment(Map<String, Object> param) throws Exception;

	// 수강 신청 등록
	int insertEnrollment(ClassEnrollmentDTO enrollment) throws Exception;

	// 특정 유저의 수강 신청 목록 조회
	List<ClassEnrollmentDTO> selectEnrollmentsByUser(int user_idx) throws Exception;

	// 특정 클래스의 수강 신청 목록 조회
	List<ClassEnrollmentDTO> selectEnrollmentsByClass(int class_idx) throws Exception;

	// 수강 신청 취소
	int updateEnrollmentStatus(Map<String, Object> param) throws Exception;

	// 수강 신청 상세 조회
	ClassEnrollmentDTO selectEnrollmentDetail(int enroll_idx) throws Exception;

	// 사용자가 해당 클래스를 이미 신청했는지 확인
	Integer checkUserEnrollment(Map<String, Object> param) throws Exception;

	// 사용자가 해당 클래스에 신청한 날짜 목록 조회
	List<String> selectUserEnrolledDates(Map<String, Object> param) throws Exception;

	// 현재 인기 클래스 불러오기
	List<CoachClassDTO> getPopularClass() throws Exception;

	// ===== 후기 관련 메서드 추가 =====

	// 클래스 후기 태그 목록 조회 (REVIEW_TAG 테이블에서 CLASS 타입만)
	List<String> selectClassReviewTags() throws Exception;

	// 후기 작성
	int insertClassReview(ReviewClassDTO review) throws Exception;

	// 사용자가 해당 클래스에 이미 후기를 작성했는지 확인
	Integer checkUserReview(Map<String, Object> param) throws Exception;

	// 사용자가 해당 클래스를 수강 완료했는지 확인 (수강 날짜가 오늘이거나 과거인지)
	Integer checkUserCompletedEnrollment(Map<String, Object> param) throws Exception;
}