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
}
