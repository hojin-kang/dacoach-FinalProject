package com.dacoach.mapper.coachClasses;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.dacoach.model.coachClasses.CoachClassDTO;

@Mapper
public interface CoachClassMapper {
	
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
	
	// 양진유 추가: 해시태그 목록 조회
	List<String> selectHashtagsByClass(int class_idx) throws Exception;
	
	// 양진유 추가: 분야 정보 조회 (대분류, 소분류)
	Map<String, Object> selectClassFieldInfo(int class_idx) throws Exception;
	
	// 양진유 추가: 지역 정보 조회 (대지역, 소지역)
	Map<String, Object> selectClassRegionInfo(int class_idx) throws Exception;
	
	// 양진유 추가: 제공자 정보 조회 (이름, 사진)
	Map<String, Object> selectProviderInfo(int provider_idx) throws Exception;
	
	// 양진유 추가: 리뷰 목록 조회
	List<Map<String, Object>> selectReviewsByClass(int class_idx) throws Exception;
	
	// 양진유 추가: 평균 평점 조회
	Double selectAvgRatingByClass(int class_idx) throws Exception;
	
	// 양진유 추가: 리뷰 개수 조회
	Integer selectReviewCountByClass(int class_idx) throws Exception;
}