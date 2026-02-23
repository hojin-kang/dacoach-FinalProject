package com.dacoach.service.adminCoach;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dacoach.mapper.adminCoach.AdminCoachMapper;
import com.dacoach.model.admin.EmbeddedUserDTO;
import com.dacoach.model.coach.CoachDTO;
import com.dacoach.model.company.CertDTO;
import com.dacoach.model.qna.QnaDTO;
import com.dacoach.model.qna.Qna_aDTO;
import com.dacoach.model.report.ReportDTO;
import com.dacoach.model.users.UsersDTO;

@Service
public class AdminCoachServiceImple implements AdminCoachService {

	@Autowired
	private AdminCoachMapper adminCoachMapper;
	
	//로그인 관리
	@Override
	public UsersDTO adminLogin(Map<String, String> params) throws Exception {
		return adminCoachMapper.adminLogin(params);
	}
	
	//메인화면(대시보드)
	@Override
	public int dailySales() throws Exception {
		Integer result=adminCoachMapper.dailySales();
		return (result==null)?0:result;
	}
	
	@Override
	public int newUsers() throws Exception {
		return adminCoachMapper.newUsers();
	}
	
	@Override
	public int pendingReports() throws Exception {
		return adminCoachMapper.pendingReports();
	}
	
	@Override
	public int inactiveCompany() throws Exception {
		return adminCoachMapper.inactiveCompany();
	}
	
	@Override
	public int reportCount() throws Exception {
		return adminCoachMapper.reportCount();
	}
	
	@Override
	public int companyWaitCount() throws Exception {
		return adminCoachMapper.companyWaitCount();
	}
	
	@Override
	public int keywordReviewCount() throws Exception {
		return adminCoachMapper.keywordReviewCount();
	}
	
	@Override
	public int getQnaWaitCount() throws Exception {
		return adminCoachMapper.getQnaWaitCount();
	}
	
	@Override
	public List<Map<String, Object>> getTopCoachInterests() throws Exception {
		return adminCoachMapper.getTopCoachInterests();
	}
	
	// 코치 프로필 및 상태 관리
	@Override
	public int getCoachTotalCnt(String keyword) throws Exception {
		int count=adminCoachMapper.getCoachTotalCnt(keyword);
		return (count==0)?1:count;
	}
	
	@Override
	public List<Map<String, Object>> getCoachList(String keyword,String sortColumn,int start,int end) throws Exception {
		return adminCoachMapper.getCoachList(keyword,sortColumn,start,end);
	}
	
	@Override
	public int getCertCount() throws Exception {
		return adminCoachMapper.getCertCount();
	}
	
	@Override
	public Map<String, Object> getCoachDetail(int coach_idx) throws Exception {
		return adminCoachMapper.getCoachDetail(coach_idx);
	}
	
	@Override
	public String getCoachStatus(int user_idx) throws Exception {
		return adminCoachMapper.getCoachStatus(user_idx);
	}

	@Override
	public EmbeddedUserDTO getCoachEmbedded(int user_idx) throws Exception {
		return adminCoachMapper.getCoachEmbedded(user_idx);
	}
	
	@Override
	public int updateCoachStatus(Map<String, Object> params) throws Exception {
		return adminCoachMapper.updateCoachStatus(params);
	}
	
	@Override
	public int insertCoachSuspended(EmbeddedUserDTO dto) throws Exception {
		return adminCoachMapper.insertCoachSuspended(dto);
	}
	
	@Override
	public int updateEnddateSuspended(int user_idx) throws Exception {
		return adminCoachMapper.updateEnddateSuspended(user_idx);
	}
	
	//코치 자격증 관리
	@Override
	public List<CertDTO> getWaitCertList(int user_idx) throws Exception {
		return adminCoachMapper.getWaitCertList(user_idx);
	}
	
	@Override
	public int updateCertStatus(CertDTO dto) throws Exception {
		return adminCoachMapper.updateCertStatus(dto);
	}
	
	@Override
	public List<CertDTO> getCertList(int user_idx) throws Exception {
		return adminCoachMapper.getCertList(user_idx);
	}
	
	//매칭관리
	@Override
	public Map<String, Object> getMatchStatusCounts() throws Exception {
		return adminCoachMapper.getMatchStatusCounts();
	}
	
	@Override
	public int getMatchTotalCnt(String keyword) throws Exception {
		int count=adminCoachMapper.getMatchTotalCnt(keyword);
		return (count==0)?1:count;
	}
	
	@Override
	public List<Map<String, Object>> getMatchList(String keyword, int start, int end) throws Exception {
		return adminCoachMapper.getMatchList(keyword, start, end);
	}
	
	//코치 상세정보 화면
	@Override
	public int getCoachInfoTotalCnt() throws Exception {
		int count=adminCoachMapper.getCoachInfoTotalCnt();
		return (count==0)?1:count;
	}
	
	@Override
	public List<Map<String, Object>> coachInfoList(int start,int end) throws Exception {
		return adminCoachMapper.coachInfoList(start,end);
	}
	
	@Override
	public CoachDTO coachInfoDetail(int coach_idx) throws Exception {
		return adminCoachMapper.coachInfoDetail(coach_idx);
	}
	
	@Override
	public List<String> coachInfoHashtag(int coach_idx) throws Exception {
		return adminCoachMapper.coachInfoHashtag(coach_idx);
	}
	
	@Override
	public int coachCount() throws Exception {
		return adminCoachMapper.coachCount();
	}
	
	@Override
	public double avgRating() throws Exception {
		return adminCoachMapper.avgRating();
	}
	
	@Override
	public int totalTokens() throws Exception {
		Integer result = adminCoachMapper.totalTokens();
		return (result==null)?0:result;
	}

}
