package com.dacoach.mapper.admin;

import java.util.*;

import org.apache.ibatis.annotations.Mapper;

import com.dacoach.model.admin.EmbeddedUserDTO;
import com.dacoach.model.coach.CoachDTO;
import com.dacoach.model.company.CertDTO;
import com.dacoach.model.qna.QnaDTO;
import com.dacoach.model.qna.Qna_aDTO;
import com.dacoach.model.report.ReportDTO;
import com.dacoach.model.users.UsersDTO;

@Mapper
public interface AdminMapper {
	//로그인 관리
	public UsersDTO adminLogin(Map<String, String> params) throws Exception;
	
	// 코치 프로필 및 상태 관리
	public int getCoachTotalCnt() throws Exception;
	public List<Map<String,Object>> getCoachList(int start,int end) throws Exception;
	public int getCertCount() throws Exception;
	public Map<String,Object> getCoachDetail(int coach_idx) throws Exception;
	public String getCoachStatus(int user_idx) throws Exception;
	public EmbeddedUserDTO getCoachEmbedded(int user_idx) throws Exception;
	public int updateCoachStatus(Map<String, Object> params) throws Exception;
	public int insertCoachSuspended(EmbeddedUserDTO dto) throws Exception;
	public int updateEnddateSuspended(int user_idx) throws Exception;
	
	//코치 자격증 관리
	public List<CertDTO> getWaitCertList(int user_idx) throws Exception;
	public int updateCertStatus(CertDTO dto) throws Exception;
	public List<CertDTO> getCertList(int user_idx) throws Exception;
	
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
	public List<Map<String, Object>> getNoticeList(String keyword) throws Exception;
	public int insertNotice(QnaDTO dto) throws Exception;
	public QnaDTO getNoticeContent(int qna_idx) throws Exception;
	public int updateNotice(QnaDTO dto) throws Exception;
	public int deleteNotice(int qna_idx) throws Exception;
	
	//QnA 관리
	public List<Map<String, Object>> getQnaList(String keyword) throws Exception;
	public Map<String, Object> getQnaContent(int qna_idx) throws Exception;
	public int insertQnaAnswer(Qna_aDTO dto) throws Exception;
	public int updateQnaAnswer(Qna_aDTO dto) throws Exception;
	public int deleteQnaAnswer(int qna_a_idx) throws Exception;
	
	//분야 카테고리 관리
	public List<Map<String, Object>> getMajorField() throws Exception;
	public List<Map<String, Object>> getMinorField(int major_field_idx) throws Exception;
	public int insertMinorField(Map<String, Object> params) throws Exception;
	public int updateMinorField(Map<String, Object> params) throws Exception;
	
	//신고 관리
	public List<Map<String,Object>> reportList(String status) throws Exception;
	public Map<String, Object> reportContent(int report_idx) throws Exception;
	public int getCoachIdx (int user_idx) throws Exception;
	public int getCompanyIdx (int user_idx) throws Exception;
	public int updateReport(ReportDTO dto) throws Exception;
	
	//메인화면
	public Integer dailySales() throws Exception;
	public int newUsers() throws Exception;
	public int pendingReports() throws Exception;
	public int inactiveCompany() throws Exception;
	
	//통계관리 화면
	public Integer monthlySales() throws Exception;
	public Integer lastMonthSales() throws Exception;
	public long avgPayAmount() throws Exception;
	public int pendingRefundCount() throws Exception;
	public List<Map<String,Object>> weeklySales() throws Exception;
	public List<Map<String,Object>> revenueByField() throws Exception;
	public List<Map<String,Object>> payTypeStats() throws Exception;
	public Integer monthlyCancelAmount() throws Exception;
	
	//코치 상세정보 화면
	public List<Map<String,Object>> coachInfoList() throws Exception;
	public CoachDTO coachInfoDetail(int coach_idx) throws Exception;
	public List<String> coachInfoHashtag(int coach_idx) throws Exception;
	public int coachCount() throws Exception;
	public double avgRating() throws Exception;
	public Integer totalTokens() throws Exception;
}
