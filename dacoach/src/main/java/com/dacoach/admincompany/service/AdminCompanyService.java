package com.dacoach.admincompany.service;

import java.util.List;
import java.util.Map;

import com.dacoach.model.admin.EmbeddedUserDTO;
import com.dacoach.model.company.CertDTO;

public interface AdminCompanyService {

	//기업 관리
    List<Map<String, Object>> getCompanyList(Map<String,Object> param);
    Map<String, Object> getCompanyDetail(int usersIdx);
    void updateCompanyFullStatus(Map<String, Object> params, EmbeddedUserDTO dto);
    //기업 계정상태 관리
    int updateCompanyStatus(Map<String, Object> params);
    int insertCompanySuspended(EmbeddedUserDTO dto);
    int updateEnddateSuspended(int user_idx);
    //기업 디테일
    void approveCompanyLogic(Map<String, Object> params);
    int getCompanyTotalCnt();
    
	String getCertFilePath(int userIdx);
	int saveCert(CertDTO dto);
	boolean hasCert(int userIdx);
    
    //클래스 관리
    List<Map<String, Object>> getClassPage(Map<String, Object> param);
    int getClassTotalCnt();
    Map<String, Object> getClassDetail(int classIdx);
    List<Map<String, Object>> getClassReviews(int classIdx);
    int deleteReview(int reviewIdx);
}