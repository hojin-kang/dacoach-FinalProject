package com.dacoach.service.admin;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dacoach.mapper.admin.AdminMapper;
import com.dacoach.model.admin.EmbeddedUserDTO;
import com.dacoach.model.coach.CoachDTO;
import com.dacoach.model.company.CertDTO;
import com.dacoach.model.qna.QnaDTO;
import com.dacoach.model.qna.Qna_aDTO;
import com.dacoach.model.report.ReportDTO;
import com.dacoach.model.users.UsersDTO;

@Service
public class AdminServiceImple implements AdminService {

	@Autowired
	private AdminMapper adminMapper;
	
	@Override
	public UsersDTO adminLogin(Map<String, String> params) throws Exception {
		return adminMapper.adminLogin(params);
	}
	
	@Override
	public int getCoachTotalCnt() throws Exception {
		int count=adminMapper.getCoachTotalCnt();
		return (count==0)?1:count;
	}
	
	@Override
	public List<Map<String, Object>> getCoachList(int start,int end) throws Exception {
		return adminMapper.getCoachList(start,end);
	}
	
	@Override
	public int getCertCount() throws Exception {
		return adminMapper.getCertCount();
	}
	
	@Override
	public Map<String, Object> getCoachDetail(int coach_idx) throws Exception {
		return adminMapper.getCoachDetail(coach_idx);
	}
	
	@Override
	public String getCoachStatus(int user_idx) throws Exception {
		return adminMapper.getCoachStatus(user_idx);
	}

	@Override
	public EmbeddedUserDTO getCoachEmbedded(int user_idx) throws Exception {
		return adminMapper.getCoachEmbedded(user_idx);
	}
	
	@Override
	public int updateCoachStatus(Map<String, Object> params) throws Exception {
		return adminMapper.updateCoachStatus(params);
	}
	
	@Override
	public int insertCoachSuspended(EmbeddedUserDTO dto) throws Exception {
		return adminMapper.insertCoachSuspended(dto);
	}
	
	@Override
	public int updateEnddateSuspended(int user_idx) throws Exception {
		return adminMapper.updateEnddateSuspended(user_idx);
	}
	
	@Override
	public List<CertDTO> getWaitCertList(int user_idx) throws Exception {
		return adminMapper.getWaitCertList(user_idx);
	}
	
	@Override
	public int updateCertStatus(CertDTO dto) throws Exception {
		return adminMapper.updateCertStatus(dto);
	}
	
	@Override
	public List<CertDTO> getCertList(int user_idx) throws Exception {
		return adminMapper.getCertList(user_idx);
	}
	
	@Override
	public List<Map<String, Object>> getKeywordType() throws Exception {
		return adminMapper.getKeywordType();
	}
	
	@Override
	public List<String> getKeywordName(String keyword_type) throws Exception {
		return adminMapper.getKeywordName(keyword_type);	
	}
	
	@Override
	public List<Map<String, Object>> getTypeReview(String keyword_type) throws Exception {
		List<Map<String, Object>> typeReview = adminMapper.getTypeReview(keyword_type);
		
		for (Map<String, Object> review : typeReview) {
			int user_idx = Integer.parseInt(String.valueOf(review.get("REVIEWER_IDX")));
			
			Integer coach_idx = adminMapper.selectCoachidx(user_idx);
			
			review.put("coach_idx", coach_idx);
		}
		return typeReview;
	}
	
	@Override
	public int insertKeyword(Map<String, String> params) throws Exception {
		return adminMapper.insertKeyword(params);
	}
	
	@Override
	public int deleteKeyword(String keyword_name) throws Exception {
		return adminMapper.deleteKeyword(keyword_name);
	}
	
	@Override
	public int selectCoachidx(int user_idx) throws Exception {
		return adminMapper.selectCoachidx(user_idx);
	}
	
	@Override
	public int deleteReviewCoach(int review_idx) throws Exception {
		return adminMapper.deleteReviewCoach(review_idx);
	}
	
	@Override
	public int deleteReviewClass(int review_idx) throws Exception {
		return adminMapper.deleteReviewClass(review_idx);
	}
	
	@Override
	public List<Map<String, Object>> getNoticeList(String keyword) throws Exception {
		return adminMapper.getNoticeList(keyword);
	}
	
	@Override
	public int insertNotice(QnaDTO dto) throws Exception {
		return adminMapper.insertNotice(dto);
	}
	
	@Override
	public QnaDTO getNoticeContent(int qna_idx) throws Exception {
		return adminMapper.getNoticeContent(qna_idx);
	}
	
	@Override
	public int updateNotice(QnaDTO dto) throws Exception {
		return adminMapper.updateNotice(dto);
	}
	
	@Override
	public int deleteNotice(int qna_idx) throws Exception {
		return adminMapper.deleteNotice(qna_idx);
	}
	
	@Override
	public List<Map<String, Object>> getQnaList(String keyword) throws Exception {
		return adminMapper.getQnaList(keyword);
	}
	
	@Override
	public Map<String, Object> getQnaContent(int qna_idx) throws Exception {
		return adminMapper.getQnaContent(qna_idx);
	}
	
	@Override
	public int insertQnaAnswer(Qna_aDTO dto) throws Exception {
		return adminMapper.insertQnaAnswer(dto);
	}
	
	@Override
	public int updateQnaAnswer(Qna_aDTO dto) throws Exception {
		return adminMapper.updateQnaAnswer(dto);
	}
	
	@Override
	public int deleteQnaAnswer(int qna_a_idx) throws Exception {
		return adminMapper.deleteQnaAnswer(qna_a_idx);
	}
	
	@Override
	public List<Map<String, Object>> getMajorField() throws Exception {
		return adminMapper.getMajorField();
	}
	
	@Override
	public List<Map<String, Object>> getMinorField(int major_field_idx) throws Exception {
		return adminMapper.getMinorField(major_field_idx);
	}
	
	@Override
	public int insertMinorField(Map<String, Object> params) throws Exception {
		return adminMapper.insertMinorField(params);
	}
	
	@Override
	public int updateMinorField(Map<String, Object> params) throws Exception {
		return adminMapper.updateMinorField(params);
	}
	
	@Override
	public List<Map<String, Object>> reportList(String status) throws Exception {
		return adminMapper.reportList(status);
	}
	
	@Override
	public Map<String, Object> reportContent(int report_idx) throws Exception {
		return adminMapper.reportContent(report_idx);
	}
	
	@Override
	public int getCoachIdx(int user_idx) throws Exception {
		return adminMapper.getCoachIdx(user_idx);
	}
	
	@Override
	public int getCompanyIdx(int user_idx) throws Exception {
		return adminMapper.getCompanyIdx(user_idx);
	}
	
	@Override
	public int updateReport(ReportDTO dto) throws Exception {
		return adminMapper.updateReport(dto);
	}
	
	@Override
	public int dailySales() throws Exception {
		Integer result=adminMapper.dailySales();
		return (result==null)?0:result;
	}
	
	@Override
	public int newUsers() throws Exception {
		return adminMapper.newUsers();
	}
	
	@Override
	public int pendingReports() throws Exception {
		return adminMapper.pendingReports();
	}
	
	@Override
	public int inactiveCompany() throws Exception {
		return adminMapper.inactiveCompany();
	}
	
	@Override
	public int monthlySales() throws Exception {
		Integer result=adminMapper.monthlySales();
		return (result==null)?0:result;
	}
	
	@Override
	public int lastMonthSales() throws Exception {
		Integer result=adminMapper.lastMonthSales();
		return (result==null)?0:result;
	}
	
	@Override
	public long avgPayAmount() throws Exception {
		return adminMapper.avgPayAmount();
	}
	
	@Override
	public int pendingRefundCount() throws Exception {
		return adminMapper.pendingRefundCount();
	}
	
	@Override
	public List<Map<String, Object>> weeklySales() throws Exception {
		return adminMapper.weeklySales();
	}
	
	@Override
	public List<Map<String, Object>> revenueByField() throws Exception {
		return adminMapper.revenueByField();
	}
	
	@Override
	public List<Map<String, Object>> payTypeStats() throws Exception {
		return adminMapper.payTypeStats();
	}
	
	@Override
	public int monthlyCancelAmount() throws Exception {
		Integer result=adminMapper.monthlyCancelAmount();
		return (result==null)?0:result;
	}
	
	@Override
	public int getCoachInfoTotalCnt() throws Exception {
		int count=adminMapper.getCoachInfoTotalCnt();
		return (count==0)?1:count;
	}
	
	@Override
	public List<Map<String, Object>> coachInfoList(int start,int end) throws Exception {
		return adminMapper.coachInfoList(start,end);
	}
	
	@Override
	public CoachDTO coachInfoDetail(int coach_idx) throws Exception {
		return adminMapper.coachInfoDetail(coach_idx);
	}
	
	@Override
	public List<String> coachInfoHashtag(int coach_idx) throws Exception {
		return adminMapper.coachInfoHashtag(coach_idx);
	}
	
	@Override
	public int coachCount() throws Exception {
		return adminMapper.coachCount();
	}
	
	@Override
	public double avgRating() throws Exception {
		return adminMapper.avgRating();
	}
	
	@Override
	public int totalTokens() throws Exception {
		Integer result = adminMapper.totalTokens();
		return (result==null)?0:result;
	}

}
