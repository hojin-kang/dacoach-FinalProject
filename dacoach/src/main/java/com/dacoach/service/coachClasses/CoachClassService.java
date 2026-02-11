package com.dacoach.service.coachClasses;

import java.util.List;
import java.util.Map;

import com.dacoach.model.coachClasses.CoachClassDTO;

public interface CoachClassService {
	
	List<Map<String, Object>> getMajorFields() throws Exception;

	List<Map<String, Object>> getMinorFields(Integer majorFieldIdx) throws Exception;

	List<Map<String, Object>> getMajorRegions() throws Exception;

	List<Map<String, Object>> getMinorRegions(Integer majorRegionIdx) throws Exception;

	// 코치 - 클래스 검색
	List<CoachClassDTO> classSearch(Integer majorField, Integer minorField, Integer majorRegion, Integer minorRegion, String q, String sort) throws Exception;
	
	// 코치 - 클래스 상세
	CoachClassDTO getClassDetail(int class_idx) throws Exception;
	
	// 양진유 추가: 해시태그 목록 조회
	List<String> getHashtagsByClass(int class_idx) throws Exception;
	
	// 양진유 추가: 분야 정보 조회
	Map<String, Object> getClassFieldInfo(int class_idx) throws Exception;
	
	// 양진유 추가: 지역 정보 조회
	Map<String, Object> getClassRegionInfo(int class_idx) throws Exception;
	
	// 양진유 추가: 제공자 정보 조회
	Map<String, Object> getProviderInfo(int provider_idx) throws Exception;
	
	// 양진유 추가: 리뷰 목록 조회
	List<Map<String, Object>> getReviewsByClass(int class_idx) throws Exception;
	
	// 양진유 추가: 평균 평점 조회
	Double getAvgRatingByClass(int class_idx) throws Exception;
	
	// 양진유 추가: 리뷰 개수 조회
	Integer getReviewCountByClass(int class_idx) throws Exception;
}