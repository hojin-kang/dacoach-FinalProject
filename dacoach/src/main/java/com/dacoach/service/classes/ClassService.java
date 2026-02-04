package com.dacoach.service.classes;

import java.util.List;
import java.util.Map;
import com.dacoach.model.classes.ClassDTO;

public interface ClassService {

	int classRegister(ClassDTO classDTO) throws Exception;

	List<Map<String, Object>> getMajorFields() throws Exception;

	List<Map<String, Object>> getMinorFields(Integer majorFieldIdx) throws Exception;

	List<Map<String, Object>> getMajorRegions() throws Exception;

	List<Map<String, Object>> getMinorRegions(Integer majorRegionIdx) throws Exception;
	
	List<ClassDTO> getClassesByProvider(Integer providerIdx) throws Exception;
	
	// 코치 - 클래스 검색
	List<ClassDTO> classSearch(Integer majorField, Integer minorField, Integer majorRegion, Integer minorRegion, String q, String sort) throws Exception;
	
	// 코치 - 클래스 상세
	ClassDTO getClassDetail(int class_idx) throws Exception;

}