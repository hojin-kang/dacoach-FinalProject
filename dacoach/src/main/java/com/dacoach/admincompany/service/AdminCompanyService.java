package com.dacoach.admincompany.service;

import java.util.List;
import java.util.Map;

public interface AdminCompanyService {

    // 기업 회원 관리
    List<Map<String, Object>> getCompanyList();
    Map<String, Object> getCompanyDetail(long usersIdx);
    void updateCompanyStatus(long usersIdx, String status, String startDate, String endDate, String reason);
    void approveCert(long usersIdx);
    
    // 클래스 목록
    List<Map<String, Object>> getClassPage(Map<String, Object> param);
    int getClassTotalCnt();
    
    // 클래스 상세 (간소화 버전)
    Map<String, Object> getClassDetail(int classIdx);
    List<Map<String, Object>> getClassReviews(int classIdx);
    void deleteReview(int reviewIdx);
}