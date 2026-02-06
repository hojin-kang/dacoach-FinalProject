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

	List<ClassDTO> getClassesByProvider(Integer providerIdx) throws Exception;

	ClassDTO getClassDetail(int class_idx) throws Exception;

	Map<String, Object> getClassStats(int class_idx, int provider_idx) throws Exception;

	// ⭐ 클래스 수정
	int classUpdate(ClassDTO classDTO, String hashtags) throws Exception;

}