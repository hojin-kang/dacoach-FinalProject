package com.dacoach.mapper.adminKeyword;

import java.util.*;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface KeywordMapper {
	
	// 검열 키워드 관리
	public List<Map<String,Object>> getKeywordType() throws Exception;
	public List<String> getKeywordName(String keyword_type) throws Exception;
	public List<Map<String,Object>> getTypeReview(String keyword_type) throws Exception;
	public int insertKeyword(Map<String, String> params) throws Exception;
	public int deleteKeyword(String keyword_name) throws Exception;
	public int selectCoachidx(int user_idx) throws Exception;
	public int deleteReviewCoach(int review_idx) throws Exception;
	public int deleteReviewClass(int review_idx) throws Exception;

	
	//분야 카테고리 관리
	public List<Map<String, Object>> getMajorField() throws Exception;
	public List<Map<String, Object>> getMinorField(int major_field_idx) throws Exception;
	public int insertMinorField(Map<String, Object> params) throws Exception;
	public int updateMinorField(Map<String, Object> params) throws Exception;
	

	
}
