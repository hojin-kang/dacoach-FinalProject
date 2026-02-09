package com.dacoach.admincompany.service;

import java.util.List;
import java.util.Map;

public interface AdminCompanyService {

	//기업 관리
    List<Map<String, Object>> getCompanyList();
    Map<String, Object> getCompanyDetail(long usersIdx);
    void updateCompanyStatus(long usersIdx, String status, String startDate, String endDate, String reason);
    void approveCert(long usersIdx);
    
    //클래스 관리
    List<Map<String, Object>> getClassPage(Map<String, Object> param);
    int getClassTotalCnt();
    Map<String, Object> getClassDetail(int classIdx);
    List<Map<String, Object>> getClassReviews(int classIdx);
    int deleteReview(int reviewIdx);
}