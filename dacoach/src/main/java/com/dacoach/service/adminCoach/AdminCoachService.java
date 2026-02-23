package com.dacoach.service.adminCoach;

import java.util.*;

import com.dacoach.model.admin.EmbeddedUserDTO;
import com.dacoach.model.coach.CoachDTO;
import com.dacoach.model.company.CertDTO;
import com.dacoach.model.users.UsersDTO;


public interface AdminCoachService {
	
	//로그인 관리
	public UsersDTO adminLogin(Map<String, String> params) throws Exception;
	
	//메인화면(대시보드)
	public int dailySales() throws Exception;
	public int newUsers() throws Exception;
	public int pendingReports() throws Exception;
	public int inactiveCompany() throws Exception;
	
	public int reportCount() throws Exception;
	public int companyWaitCount() throws Exception;
	
	public int keywordReviewCount() throws Exception;
	public int getQnaWaitCount() throws Exception;
	
	public List<Map<String,Object>> getTopCoachInterests() throws Exception;
	
	// 코치 프로필 및 상태 관리
	public int getCoachTotalCnt(String keyword) throws Exception;
	List<Map<String,Object>> getCoachList(String keyword,String sortColumn,int start,int end) throws Exception;
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
	
	//매칭관리
	public Map<String,Object> getMatchStatusCounts() throws Exception;
	public int getMatchTotalCnt(String keyword) throws Exception;
	public List<Map<String,Object>> getMatchList(String keyword,int start,int end) throws Exception;
	
	//코치 상세정보 화면
	public int getCoachInfoTotalCnt() throws Exception;
	List<Map<String,Object>> coachInfoList(int start,int end) throws Exception;
	public CoachDTO coachInfoDetail(int coach_idx) throws Exception;
	List<String> coachInfoHashtag(int coach_idx) throws Exception;
	public int coachCount() throws Exception;
	public double avgRating() throws Exception;
	public int totalTokens() throws Exception;
}
