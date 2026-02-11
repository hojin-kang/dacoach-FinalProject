package com.dacoach.mapper.admin.company;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.dacoach.model.company.CertDTO;

@Mapper
public interface AdminCompanyMapper {

    // 기업 회원 관리
    List<Map<String, Object>> companyList();
    Map<String, Object> companyDetail(@Param("usersIdx") int usersIdx);
    String getCertFilePath(@Param("userIdx") int userIdx);
    int insertCert(CertDTO dto);
    int hasCert(@Param("userIdx") int userIdx);
    
    int countCompanyTotal();    
    int updateUserStatus(@Param("userIdx") int userIdx, @Param("status") String status);
    int insertEmbeddedHistory(Map<String, Object> param);
    int closeLatestEmbeddedHistory(@Param("userIdx") int userIdx);
    
    int countCertByUserAndType(@Param("userIdx") int userIdx, @Param("certType") String certType);
    int updateCertStatus(@Param("user_idx") int userIdx, @Param("cert_status") String certStatus);
    int insertCertStatus(@Param("user_idx") int userIdx, @Param("cert_status") String certStatus);
    
    // 클래스 목록
    List<Map<String, Object>> selectClassPage(Map<String, Object> param);
    int countClassTotal();
    
    
    // 클래스 상세 
    Map<String, Object> classDetail(@Param("classIdx") int classIdx);
    Map<String, Object> selectClassRatingSummary(@Param("classIdx") int classIdx);
    int selectClassEnrollCount(@Param("classIdx") int classIdx);
    
    // 클래스 리뷰 관리
    List<Map<String, Object>> selectClassReviews(@Param("classIdx") int classIdx);
    int deleteReview(@Param("reviewIdx") int reviewIdx);
}