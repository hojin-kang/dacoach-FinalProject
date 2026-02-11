package com.dacoach.service.coachClasses;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dacoach.mapper.coachClasses.CoachClassMapper;
import com.dacoach.model.coachClasses.CoachClassDTO;

@Service
public class CoachClassServiceImple implements CoachClassService {
	
	@Autowired
	private CoachClassMapper classMapper;
	
	@Override
	public List<Map<String, Object>> getMajorFields() throws Exception {
		return classMapper.selectMajorFields();
	}

	@Override
	public List<Map<String, Object>> getMinorFields(Integer majorFieldIdx) throws Exception {
		if (majorFieldIdx == null) {
			throw new IllegalArgumentException("대분류를 선택해주세요.");
		}
		return classMapper.selectMinorFields(majorFieldIdx);
	}

	@Override
	public List<Map<String, Object>> getMajorRegions() throws Exception {
		return classMapper.selectMajorRegions();
	}

	@Override
	public List<Map<String, Object>> getMinorRegions(Integer majorRegionIdx) throws Exception {
		if (majorRegionIdx == null) {
			throw new IllegalArgumentException("대지역을 선택해주세요.");
		}
		return classMapper.selectMinorRegions(majorRegionIdx);
	}
	
	@Override
	public List<CoachClassDTO> classSearch(Integer majorField, Integer minorField, Integer majorRegion, Integer minorRegion,
			String q, String sort) throws Exception {
		Map<String, Object> param = new HashMap<>();
		param.put("majorField", majorField);
		param.put("minorField", minorField);
		param.put("majorRegion", majorRegion);
		param.put("minorRegion", minorRegion);
		param.put("q", (q == null ? null : q.trim()));
		param.put("sort", (sort == null ? "latest" : sort));

		return classMapper.classSearch(param);
	}
	
	@Override
	public CoachClassDTO getClassDetail(int classIdx) throws Exception {
	    return classMapper.getClassDetail(classIdx);
	}
	
	// 양진유 추가 메서드 부분 ---------------------
	@Override
	public List<String> getHashtagsByClass(int class_idx) throws Exception {
		return classMapper.selectHashtagsByClass(class_idx);
	}
	
	@Override
	public Map<String, Object> getClassFieldInfo(int class_idx) throws Exception {
		return classMapper.selectClassFieldInfo(class_idx);
	}
	
	@Override
	public Map<String, Object> getClassRegionInfo(int class_idx) throws Exception {
		return classMapper.selectClassRegionInfo(class_idx);
	}
	
	@Override
	public Map<String, Object> getProviderInfo(int provider_idx) throws Exception {
		return classMapper.selectProviderInfo(provider_idx);
	}
	
	@Override
	public List<Map<String, Object>> getReviewsByClass(int class_idx) throws Exception {
		return classMapper.selectReviewsByClass(class_idx);
	}
	
	@Override
	public Double getAvgRatingByClass(int class_idx) throws Exception {
		Double avgRating = classMapper.selectAvgRatingByClass(class_idx);
		return avgRating != null ? avgRating : 0.0;
	}
	
	@Override
	public Integer getReviewCountByClass(int class_idx) throws Exception {
		Integer count = classMapper.selectReviewCountByClass(class_idx);
		return count != null ? count : 0;
	}
}