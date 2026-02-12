package com.dacoach.mapper.admin.company;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.dacoach.model.admin.EmbeddedUserDTO;
import com.dacoach.model.company.CertDTO;

@Mapper
public interface AdminCompanyMapper {

	//cert 관리
	int updateCertDetail(Map<String, Object> params);
    // 기업 회원 관리
    List<Map<String, Object>> companyList();
    Map<String, Object> companyDetail(@Param("userIdx") int usersIdx);
    String getCertFilePath(@Param("userIdx") int userIdx);
    int insertCert(CertDTO dto);
    int hasCert(@Param("userIdx") int userIdx);
    
    int countCompanyTotal();    
    //계정상태 관리
    int updateCompanyStatus(Map<String, Object> params);
    int insertCompanySuspended(EmbeddedUserDTO dto);
    int updateEnddateSuspended(@Param("userIdx")int user_idx);
    
    
    int insertEmbeddedHistory(Map<String, Object> param);
    int closeLatestEmbeddedHistory(@Param("userIdx") int userIdx);
    
    int countCertByUserAndType(@Param("userIdx") int userIdx, @Param("certType") String certType);
    
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