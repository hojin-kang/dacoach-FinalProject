package com.dacoach.mapper.classes;

import java.util.*;
import org.apache.ibatis.annotations.Mapper;
import com.dacoach.model.classes.ClassDTO;

@Mapper
public interface ClassMapper {

	// 클래스 등록
	int insertClass(ClassDTO classDTO) throws Exception;

	// 대분류 조회 (Ajax용)
	List<Map<String, Object>> selectMajorFields() throws Exception;

	// 소분류 조회 (대분류 선택시 Ajax)
	List<Map<String, Object>> selectMinorFields(Integer major_field_idx) throws Exception;

	// 대지역 조회 (Ajax용)
	List<Map<String, Object>> selectMajorRegions() throws Exception;

	// 소지역 조회 (대지역 선택시 Ajax)
	List<Map<String, Object>> selectMinorRegions(Integer major_region_idx) throws Exception;

	// 클래스 조회 (정렬 옵션 추가)
	List<ClassDTO> selectClassesByProvider(Map<String, Object> params) throws Exception;

	ClassDTO getClassDetail(int class_idx) throws Exception;

	// 클래스별 수강 신청 내역 조회
	List<Map<String, Object>> selectEnrollmentsByClass(int class_idx) throws Exception;

	// ⭐ 해시태그 관련 메서드
	// 해시태그 이름으로 IDX 찾기 (중복 체크용)
	Integer findHashtagByName(String tags) throws Exception;

	// 새 해시태그 등록
	int insertHashtag(String tags) throws Exception;

	// 클래스-해시태그 매핑 저장
	int insertClassHashtag(int class_idx, int hashtag_idx) throws Exception;

	// 클래스의 해시태그 목록 조회
	List<String> selectHashtagsByClass(int class_idx) throws Exception;

	// 클래스의 분야 정보 조회 (대분류, 소분류)
	Map<String, Object> selectClassFieldInfo(int class_idx) throws Exception;

	// 클래스의 분야 정보 조회 (대지역, 소지역)
	Map<String, Object> selectClassRegionInfo(int class_idx) throws Exception;

	// 제공자 정보 조회 (이름, 사진)
	Map<String, Object> selectProviderInfo(int provider_idx) throws Exception;

	// ⭐ 클래스 수정 관련 메서드
	// 클래스 정보 수정
	int updateClass(ClassDTO classDTO) throws Exception;

	// 클래스의 기존 해시태그 모두 삭제 (수정 시 재등록을 위해)
	int deleteClassHashtags(int class_idx) throws Exception;

	// 소지역 IDX로 대지역 정보 조회 (수정 폼 초기화용)
	Map<String, Object> getRegionInfoByMinorIdx(Integer minorRegionIdx) throws Exception;

	// ⭐ 리뷰 관련 메서드
	// 특정 클래스의 리뷰 목록 조회
	List<Map<String, Object>> selectReviewsByClass(int class_idx) throws Exception;

	// 클래스의 평균 평점 조회
	Double selectAvgRatingByClass(int class_idx) throws Exception;

	// 클래스의 리뷰 개수 조회
	Integer selectReviewCountByClass(int class_idx) throws Exception;

	// 제공자의 모든 클래스 IDX 조회
	List<Integer> selectClassIdxByProvider(int provider_idx) throws Exception;

	// ⭐ 멤버십 기반 클래스 등록 제한 관련 메서드
	// 유저의 멤버십 상세 정보 조회 (최대 클래스 개수)
	Map<String, Object> selectMembershipDetail(int user_idx) throws Exception;

	// 제공자의 활성 클래스 개수 조회 (종료되지 않은 클래스)
	Integer countActiveClassesByProvider(int provider_idx) throws Exception;
}