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
}
