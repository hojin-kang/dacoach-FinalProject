package com.dacoach.admincompany.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dacoach.mapper.admin.company.AdminCompanyMapper;
import com.dacoach.model.admin.EmbeddedUserDTO;
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
    public Map<String, Object> getCompanyDetail(int usersIdx) {
        return mapper.companyDetail(usersIdx);
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

	//기업 계정상태 관리
	@Override
	public int updateCompanyStatus(Map<String, Object> params) {
		return mapper.updateCompanyStatus(params);
	}

	@Override
	public int insertCompanySuspended(EmbeddedUserDTO dto) {
		return mapper.insertCompanySuspended(dto);
	}

	@Override
	public int updateEnddateSuspended(int user_idx) {
		return mapper.updateEnddateSuspended(user_idx);
	}

	@Override
	public void approveCompanyLogic(Map<String, Object> params) {
		params.put("status", "ACTIVE");
        mapper.updateCompanyStatus(params);
        mapper.updateCertDetail(params);
	}
	
	public void updateCompanyFullStatus(Map<String, Object> params, EmbeddedUserDTO dto) {
	    String status = (String) params.get("status");
	    int userIdx = Integer.parseInt(params.get("user_idx").toString());

	    // 1. 공통: 유저 상태 업데이트 (ACTIVE, SUSPENDED, WARNING, DANGER 모두 수행)
	    mapper.updateCompanyStatus(params);

	    // 2. 상태별 분기 처리
	    if ("ACTIVE".equals(status)) {
	        // 승인 시: 사업자 정보(날짜, 기관 등) 업데이트 + 정지 이력 종료
	        mapper.updateCertDetail(params); 
	        mapper.updateEnddateSuspended(userIdx);
	        
	    } else if ("SUSPENDED".equals(status)) {
	        // 정지 시: 정지 이력 테이블 INSERT
	        dto.setUser_idx(userIdx);
	        mapper.insertCompanySuspended(dto);
	        
	    } else {
	        // WARNING, DANGER 등: 기존 정지 이력이 있다면 종료 처리
	        mapper.updateEnddateSuspended(userIdx);
	    }
	}
}