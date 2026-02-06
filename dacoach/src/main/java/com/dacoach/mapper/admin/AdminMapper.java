package com.dacoach.mapper.admin;

import java.util.*;

import org.apache.ibatis.annotations.Mapper;

import com.dacoach.model.admin.EmbeddedUserDTO;

@Mapper
public interface AdminMapper {
	// 코치 프로필 및 상태 관리
	public List<Map<String,Object>> getCoachList() throws Exception;
	public int getCertCount() throws Exception;
	public List<Map<String,Object>> getCoachDetail(int coach_idx) throws Exception;
	public String getCoachStatus(int user_idx) throws Exception;
	public EmbeddedUserDTO getCoachEmbedded(int user_idx) throws Exception;
	public int updateCoachStatus(Map<String, Object> params) throws Exception;
	public int insertCoachSuspended(EmbeddedUserDTO dto) throws Exception;
	public int updateEnddateSuspended(int user_idx) throws Exception;
	
	// 검열 키워드 관리
	public List<Map<String,Object>> getKeywordType() throws Exception;
	public List<String> getKeywordName(String keyword_type) throws Exception;
	public List<Map<String,Object>> getTypeReview(String keyword_type) throws Exception;
	public int insertKeyword(Map<String, String> params) throws Exception;
	public int deleteKeyword(String keyword_name) throws Exception;
	public int selectCoachidx(int user_idx) throws Exception;
}
