package com.dacoach.mapper.admin.company;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface AdminCompanyMapper {

    // 1. 기업 목록
    List<Map<String, Object>> companyList();

    // 2. 기업 상세
    Map<String, Object> companyDetail(@Param("usersIdx") int usersIdx);

    // 3. CERT 관련
    int countCertByUserAndType(@Param("userIdx") long userIdx,
                               @Param("certType") String certType);

    int updateCertStatus(@Param("user_idx") long userIdx,
                         @Param("cert_status") String certStatus);

    int insertCertStatus(@Param("user_idx") long userIdx,
                         @Param("cert_status") String certStatus);

    // 4. USERS 상태 업데이트
    int updateUserStatus(@Param("userIdx") long userIdx,
                         @Param("status") String status);

    // 5. EMBEDDED_USER (정지 이력)
    int insertEmbeddedHistory(@Param("userIdx") long userIdx,
                              @Param("startDate") String startDate,
                              @Param("endDate") String endDate,
                              @Param("reason") String reason);

    int closeLatestEmbeddedHistory(@Param("userIdx") long userIdx);

    Map<String, Object> selectLatestEmbeddedByUser(@Param("userIdx") long userIdx);

    // 6. 클래스 목록 (페이징)
    List<Map<String, Object>> selectClassPage(Map<String, Object> param);

    // 7. 리뷰 요약 (필요시)
    List<Map<String, Object>> selectReviewSummaryByClassIds(List<Integer> classIds);

    // 8. 클래스 전체 개수
    int countClassTotal();
    
    // 9. 클래스 상세
    Map<String, Object> classDetail(@Param("classIdx") int classIdx);
    
    // 10. 클래스 리뷰 목록
    List<Map<String, Object>> selectClassReviews(@Param("classIdx") int classIdx);
    
    // 11. 리뷰 삭제
    int deleteReview(@Param("reviewIdx") int reviewIdx);
}