package com.dacoach.admincompany.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dacoach.mapper.admin.company.AdminCompanyMapper;
import com.dacoach.model.company.CertDTO;

@Service
public class AdminCompanyServiceImple implements AdminCompanyService {

    @Autowired
    private AdminCompanyMapper mapper;

    //기업 관리
    
    @Override
    public List<Map<String, Object>> getCompanyList() {
        return mapper.companyList();
    }

    @Override
    public Map<String, Object> getCompanyDetail(long usersIdx) {
        return mapper.companyDetail(usersIdx);
    }

    @Override
    @Transactional
    public void updateCompanyStatus(long usersIdx, String status, String startDate, String endDate, String reason) {
        // 상태 업데이트
        mapper.updateUserStatus(usersIdx, status);
        
        // 정지 상태인 경우 정지 이력 추가
        if ("SUSPENDED".equals(status)) {
            Map<String, Object> param = new HashMap<>();
            param.put("userIdx", usersIdx);
            param.put("startDate", startDate);
            param.put("endDate", endDate);
            param.put("reason", reason);
            mapper.insertEmbeddedHistory(param);
        }
        // 사용 상태로 복귀 시 최신 정지 이력 종료
        else if ("ACTIVE".equals(status)) {
            mapper.closeLatestEmbeddedHistory(usersIdx);
        }
    }

    @Override
    @Transactional
    public void approveCert(long usersIdx) {
        String certType = "사업증";
        String certStatus = "확인";
        
        // 기존 CERT 존재 여부 확인
        int cnt = mapper.countCertByUserAndType(usersIdx, certType);
        
        // 존재하면 업데이트, 없으면 새로 추가
        if (cnt > 0) {
            mapper.updateCertStatus(usersIdx, certStatus);
        } else {
            mapper.insertCertStatus(usersIdx, certStatus);
        }
        
        // 계정 상태를 ACTIVE로 변경
        mapper.updateUserStatus(usersIdx, "ACTIVE");
       
    }

    //클래스 관리
    
    @Override
    public List<Map<String, Object>> getClassPage(Map<String, Object> param) {
        return mapper.selectClassPage(param);
    }

    @Override
    public int getClassTotalCnt() {
        return mapper.countClassTotal();
    }

    @Override
    public Map<String, Object> getClassDetail(int classIdx) {
        // 기본 정보 조회
        Map<String, Object> classInfo = mapper.classDetail(classIdx);
        
        // 평점 정보 추가
        Map<String, Object> rating = mapper.selectClassRatingSummary(classIdx);
        if (rating != null) {
            classInfo.put("AVG_RATING", rating.get("AVG_RATING"));
            classInfo.put("REVIEW_COUNT", rating.get("REVIEW_COUNT"));
        } else {
            classInfo.put("AVG_RATING", 0);
            classInfo.put("REVIEW_COUNT", 0);
        }
        
        // 수강생 수 추가
        int enrollCount = mapper.selectClassEnrollCount(classIdx);
        classInfo.put("ENROLL_COUNT", enrollCount);
        
        return classInfo;
    }

    @Override
    public List<Map<String, Object>> getClassReviews(int classIdx) {
        return mapper.selectClassReviews(classIdx);
    }

    @Override
    @Transactional
    public int deleteReview(int reviewIdx) {
        return mapper.deleteReview(reviewIdx);
    }

    //파일 부분
	@Override
	public String getCertFilePath(int userIdx) {
		return mapper.getCertFilePath(userIdx);
	}

	@Override
	public int saveCert(CertDTO dto) {
		return mapper.insertCert(dto);
	}

	@Override
	public boolean hasCert(int userIdx) {
		return mapper.hasCert(userIdx)>0?true:false;
	}
}