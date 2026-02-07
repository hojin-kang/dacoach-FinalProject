package com.dacoach.admin.company.model;

import java.util.List;
import java.util.Map;

public interface AdminCompanyDAO {

    // 목록/상세
    List<Map<String, Object>> companyList();
    Map<String, Object> companyDetail(int usersIdx);

    // USERS 상태 저장
    int updateUserStatus(long userIdx, String status);

    // EMBEDDED_USER (정지 이력 누적 / 최신 종료)
    int insertEmbeddedHistory(long userIdx, String startDate, String endDate, String reason);
    int closeLatestEmbeddedHistory(long userIdx);

    // CERT (사업증 승인여부)
    int countCertByUserAndType(long userIdx, String certType);
    int updateCertStatus(long userIdx, String certStatus);
    int insertCertStatus(long userIdx, String certStatus);
    
    // 클래스 리스트, 평점
    List<Map<String, Object>> selectClassPage(Map<String, Object> param);
    int countClassTotal();
    
    // 클래스 상세
    Map<String, Object> classDetail(int classIdx);
    List<Map<String, Object>> selectClassReviews(int classIdx);
    int deleteReview(int reviewIdx);
}