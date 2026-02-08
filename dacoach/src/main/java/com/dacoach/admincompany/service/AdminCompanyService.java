package com.dacoach.admincompany.service;

import java.util.List;
import java.util.Map;

public interface AdminCompanyService {

    // 목록/상세
    List<Map<String, Object>> companyList();
    Map<String, Object> companyDetail(int usersIdx);

    // 저장 버튼: 계정상태 정지이력 저장
    void saveAccountAndSuspend(long userIdx, String status,
                               String suspendFrom, String suspendUntil, String reason);

    // 승인 버튼: 승인여부 cert
    void approveCompany(long userIdx);
    
    // 클래스 리스트, 평점
    List<Map<String, Object>> getClassPage(Map<String, Object> param);
    int getClassTotalCnt();
    
    //클래스 상세
    Map<String,Object> getClassDetail(int classIdx);
    List<Map<String, Object>> getClassReviews(int classIdx);
    void deleteReview(int reviewIdx);
}