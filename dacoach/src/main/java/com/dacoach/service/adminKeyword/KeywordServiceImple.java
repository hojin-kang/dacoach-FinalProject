package com.dacoach.service.adminKeyword;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dacoach.mapper.adminKeyword.KeywordMapper;

@Service
public class KeywordServiceImple implements KeywordService {

	@Autowired
	private KeywordMapper keywordMapper;
	
	@Override
	public List<Map<String, Object>> getKeywordType() throws Exception {
		return keywordMapper.getKeywordType();
	}
	
	@Override
	public List<String> getKeywordName(String keyword_type) throws Exception {
		return keywordMapper.getKeywordName(keyword_type);	
	}
	
	@Override
	public List<Map<String, Object>> getTypeReview(String keyword_type) throws Exception {
		List<Map<String, Object>> typeReview = keywordMapper.getTypeReview(keyword_type);
		
		for (Map<String, Object> review : typeReview) {
			int user_idx = Integer.parseInt(String.valueOf(review.get("REVIEWER_IDX")));
			
			Integer coach_idx = keywordMapper.selectCoachidx(user_idx);
			
			review.put("coach_idx", coach_idx);
		}
		return typeReview;
	}
	
	@Override
	public int insertKeyword(Map<String, String> params) throws Exception {
		return keywordMapper.insertKeyword(params);
	}
	
	@Override
	public int deleteKeyword(String keyword_name) throws Exception {
		return keywordMapper.deleteKeyword(keyword_name);
	}
	
	@Override
	public int selectCoachidx(int user_idx) throws Exception {
		return keywordMapper.selectCoachidx(user_idx);
	}
	
	@Override
	public int deleteReviewCoach(int review_idx) throws Exception {
		return keywordMapper.deleteReviewCoach(review_idx);
	}
	
	@Override
	public int deleteReviewClass(int review_idx) throws Exception {
		return keywordMapper.deleteReviewClass(review_idx);
	}
	
	
	@Override
	public List<Map<String, Object>> getMajorField() throws Exception {
		return keywordMapper.getMajorField();
	}
	
	@Override
	public List<Map<String, Object>> getMinorField(int major_field_idx) throws Exception {
		return keywordMapper.getMinorField(major_field_idx);
	}
	
	@Override
	public int insertMinorField(Map<String, Object> params) throws Exception {
		return keywordMapper.insertMinorField(params);
	}
	
	@Override
	public int updateMinorField(Map<String, Object> params) throws Exception {
		return keywordMapper.updateMinorField(params);
	}

}
