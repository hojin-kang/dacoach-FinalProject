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
	
	// 클래스 조회
	List<ClassDTO> selectClassesByProvider(Integer provider_idx) throws Exception;

	// 코치 - 클래스 검색
	List<ClassDTO> classSearch(Map<String, Object> param) throws Exception;
	
	// 코치 - 클래스 상세
	ClassDTO getClassDetail(int class_idx) throws Exception;

}