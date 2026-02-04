package com.dacoach.service.classes;

import java.util.List;
import java.util.Map;
import com.dacoach.model.classes.ClassDTO;

public interface ClassService {

	int classRegister(ClassDTO classDTO);

	List<Map<String, Object>> getMajorFields();

	List<Map<String, Object>> getMinorFields(Integer majorFieldIdx);

	List<Map<String, Object>> getMajorRegions();

	List<Map<String, Object>> getMinorRegions(Integer majorRegionIdx);
	
	List<ClassDTO> getClassesByProvider(Integer providerIdx);
	
	// 코치 - 클래스 검색
	List<ClassDTO> searchCoachClasses(Integer minorField, Integer minorRegion, String q, String sort);

}