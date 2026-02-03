package com.dacoach.mapper.classes;

import java.util.*;
import org.apache.ibatis.annotations.Mapper;
import com.dacoach.model.classes.ClassDTO;

@Mapper
public interface ClassMapper {

	// 클래스 등록
	int insertClass(ClassDTO classDTO);

	// 대분류 조회 (Ajax용)
	List<Map<String, Object>> selectMajorFields();

	// 소분류 조회 (대분류 선택시 Ajax)
	List<Map<String, Object>> selectMinorFields(Integer major_field_idx);

	// 대지역 조회 (Ajax용)
	List<Map<String, Object>> selectMajorRegions();

	// 소지역 조회 (대지역 선택시 Ajax)
	List<Map<String, Object>> selectMinorRegions(Integer major_region_idx);
}