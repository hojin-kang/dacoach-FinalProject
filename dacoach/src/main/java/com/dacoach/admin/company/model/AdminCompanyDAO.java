package com.dacoach.admin.company.model;

import java.util.List;
import java.util.Map;

public interface AdminCompanyDAO {

    // 기업 회원 관리
    List<Map<String, Object>> companyList();
    Map<String, Object> companyDetail(long usersIdx);
    
    int updateUserStatus(long userIdx, String status);
    int insertEmbeddedHistory(Map<String, Object> param);
    int closeLatestEmbeddedHistory(long userIdx);
    
    int countCertByUserAndType(long userIdx, String certType);
    int updateCertStatus(long userIdx, String certStatus);
    int insertCertStatus(long userIdx, String certStatus);
    
    // 클래스 목록
    List<Map<String, Object>> selectClassPage(Map<String, Object> param);
    int countClassTotal();
    
    // 클래스 상세 (간소화 버전)
    Map<String, Object> classDetail(int classIdx);
    Map<String, Object> selectClassRatingSummary(int classIdx);
    int selectClassEnrollCount(int classIdx);
    
    // 클래스 리뷰 관리
    List<Map<String, Object>> selectClassReviews(int classIdx);
    int deleteReview(int reviewIdx);
}