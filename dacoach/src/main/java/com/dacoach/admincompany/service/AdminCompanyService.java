package com.dacoach.admincompany.service;

import java.util.List;
import java.util.Map;

import com.dacoach.model.company.CertDTO;

public interface AdminCompanyService {

	//기업 관리
    List<Map<String, Object>> getCompanyList();
    Map<String, Object> getCompanyDetail(int usersIdx);
    void updateCompanyStatus(int usersIdx, String status, String startDate, String endDate, String reason);
    void approveCert(int usersIdx);
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