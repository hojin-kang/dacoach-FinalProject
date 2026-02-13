package com.dacoach.service.admin;

import java.util.*;

import com.dacoach.model.admin.EmbeddedUserDTO;
import com.dacoach.model.company.CertDTO;
import com.dacoach.model.qna.QnaDTO;
import com.dacoach.model.qna.Qna_aDTO;
import com.dacoach.model.report.ReportDTO;
import com.dacoach.model.users.UsersDTO;


public interface AdminService {
	
	//로그인 관리
	public UsersDTO adminLogin(Map<String, String> params) throws Exception;
	
	// 코치 프로필 및 상태 관리
	List<Map<String,Object>> getCoachList() throws Exception;
	public int getCertCount() throws Exception;
	Map<String,Object> getCoachDetail(int coach_idx) throws Exception;
	public String getCoachStatus(int user_idx) throws Exception;
	public EmbeddedUserDTO getCoachEmbedded(int user_idx) throws Exception;
	public int updateCoachStatus(Map<String, Object> params) throws Exception;
	public int insertCoachSuspended(EmbeddedUserDTO dto) throws Exception;
	public int updateEnddateSuspended(int user_idx) throws Exception;
	
	//코치 자격증 관리
	List<CertDTO> getWaitCertList(int user_idx) throws Exception;
	public int updateCertStatus(CertDTO dto) throws Exception;
	List<CertDTO> getCertList(int user_idx) throws Exception;
	
	
	// 검열 키워드 관리
	List<Map<String,Object>> getKeywordType() throws Exception;
	List<String> getKeywordName(String keyword_type) throws Exception;
	List<Map<String,Object>> getTypeReview(String keyword_type) throws Exception;
	public int insertKeyword(Map<String, String> params) throws Exception;
	public int deleteKeyword(String keyword_name) throws Exception;
	public int selectCoachidx(int user_idx) throws Exception;
	public int deleteReviewCoach(int review_idx) throws Exception;
	public int deleteReviewClass(int review_idx) throws Exception;
	
	//공지 관리(QnA 테이블 사용)
	List<Map<String, Object>> getNoticeList(String keyword) throws Exception;
	public int insertNotice(QnaDTO dto) throws Exception;
	public QnaDTO getNoticeContent(int qna_idx) throws Exception;
	public int updateNotice(QnaDTO dto) throws Exception;
	public int deleteNotice(int qna_idx) throws Exception;
	
	//QnA 관리
	List<Map<String, Object>> getQnaList(String keyword) throws Exception;
	Map<String, Object> getQnaContent(int qna_idx) throws Exception;
	public int insertQnaAnswer(Qna_aDTO dto) throws Exception;
	public int updateQnaAnswer(Qna_aDTO dto) throws Exception;
	public int deleteQnaAnswer(int qna_a_idx) throws Exception;
	
	//분야 카테고리 관리
	List<Map<String, Object>> getMajorField() throws Exception;
	List<Map<String, Object>> getMinorField(int major_field_idx) throws Exception;
	public int insertMinorField(Map<String, Object> params) throws Exception;
	public int updateMinorField(Map<String, Object> params) throws Exception;
	
	//신고관리
	public List<Map<String,Object>> reportList() throws Exception;
	public Map<String, Object> reportContent(int report_idx) throws Exception;
	public int getCoachIdx (int user_idx) throws Exception;
	public int getCompanyIdx (int user_idx) throws Exception;
	public int updateReport(ReportDTO dto) throws Exception;
}
