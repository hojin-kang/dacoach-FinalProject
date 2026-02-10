package com.dacoach.service.classes;

import java.util.List;
import java.util.Map;
import com.dacoach.model.classes.ClassDTO;

public interface ClassService {

	int classRegister(ClassDTO classDTO, String hashtags) throws Exception;

	List<Map<String, Object>> getMajorFields() throws Exception;

	List<Map<String, Object>> getMinorFields(Integer majorFieldIdx) throws Exception;

	List<Map<String, Object>> getMajorRegions() throws Exception;

	List<Map<String, Object>> getMinorRegions(Integer majorRegionIdx) throws Exception;

	// 정렬 파라미터 추가
	List<ClassDTO> getClassesByProvider(Integer providerIdx, String sort) throws Exception;

	ClassDTO getClassDetail(int class_idx) throws Exception;

	Map<String, Object> getProviderInfo(int provider_idx) throws Exception;

	Map<String, Object> getClassStats(int class_idx, int provider_idx) throws Exception;

	// ⭐ 클래스 수정
	int classUpdate(ClassDTO classDTO, String hashtags) throws Exception;

	// ⭐ 해시태그 목록 조회
	List<String> getHashtagsByClass(int class_idx) throws Exception;

	// ⭐ 분야 정보 조회
	Map<String, Object> getClassFieldInfo(int class_idx) throws Exception;

	// ⭐ 지역 정보 조회
	Map<String, Object> getClassRegionInfo(int class_idx) throws Exception;

	// ⭐ 지역 정보 조회
	Map<String, Object> getRegionInfoByMinorIdx(Integer minorRegionIdx) throws Exception;

	// ⭐ 리뷰 관련 메서드
	// 특정 클래스의 리뷰 목록 조회
	List<Map<String, Object>> getReviewsByClass(int class_idx) throws Exception;

	// 클래스의 평균 평점 조회
	Double getAvgRatingByClass(int class_idx) throws Exception;

	// 클래스의 리뷰 개수 조회
	Integer getReviewCountByClass(int class_idx) throws Exception;

	// 제공자의 모든 클래스의 리뷰 목록 조회
	List<Map<String, Object>> getAllReviewsByProvider(int provider_idx) throws Exception;

}