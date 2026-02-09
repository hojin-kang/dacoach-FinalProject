package com.dacoach.mapper.admin;

import java.util.*;

import org.apache.ibatis.annotations.Mapper;

import com.dacoach.model.admin.EmbeddedUserDTO;
import com.dacoach.model.qna.QnaDTO;
import com.dacoach.model.users.UsersDTO;

@Mapper
public interface AdminMapper {
	//로그인 관리
	public UsersDTO adminLogin(Map<String, String> params) throws Exception;
	
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
	public int deleteReviewCoach(int review_idx) throws Exception;
	public int deleteReviewClass(int review_idx) throws Exception;
	
	//공지 관리(QnA 테이블 사용)
	public List<Map<String, Object>> getNoticeList() throws Exception;
	public int insertNotice(QnaDTO dto) throws Exception;
	public QnaDTO getNoticeContent(int qna_idx) throws Exception;
	
	//분야 카테고리 관리
	public List<Map<String, Object>> getMajorField() throws Exception;
	public List<Map<String, Object>> getMinorField(int major_field_idx) throws Exception;
	
}
