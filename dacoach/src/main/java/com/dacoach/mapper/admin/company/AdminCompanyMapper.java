package com.dacoach.mapper.admin.company;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface AdminCompanyMapper {

    // 기업 회원 관리
    List<Map<String, Object>> companyList();
    Map<String, Object> companyDetail(@Param("usersIdx") long usersIdx);
    int countCompanyTotal();    
    int updateUserStatus(@Param("userIdx") long userIdx, @Param("status") String status);
    int insertEmbeddedHistory(Map<String, Object> param);
    int closeLatestEmbeddedHistory(@Param("userIdx") long userIdx);
    
    int countCertByUserAndType(@Param("userIdx") long userIdx, @Param("certType") String certType);
    int updateCertStatus(@Param("user_idx") long userIdx, @Param("cert_status") String certStatus);
    int insertCertStatus(@Param("user_idx") long userIdx, @Param("cert_status") String certStatus);
    
    // 클래스 목록
    List<Map<String, Object>> selectClassPage(Map<String, Object> param);
    int countClassTotal();
    
    
    // 클래스 상세 (간소화 버전)
    Map<String, Object> classDetail(@Param("classIdx") int classIdx);
    Map<String, Object> selectClassRatingSummary(@Param("classIdx") int classIdx);
    int selectClassEnrollCount(@Param("classIdx") int classIdx);
    
    // 클래스 리뷰 관리
    List<Map<String, Object>> selectClassReviews(@Param("classIdx") int classIdx);
    int deleteReview(@Param("reviewIdx") int reviewIdx);
}