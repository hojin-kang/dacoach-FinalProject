package com.dacoach.admincompany.service;

import java.sql.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dacoach.admin.company.model.AdminCompanyDAO;
import com.dacoach.admin.company.model.AdminCompanyRowDTO;
import com.dacoach.model.company.CompanyDTO;

@Service
public class AdminCompanyServiceImple implements AdminCompanyService {

	private final AdminCompanyDAO dao;

	  public AdminCompanyServiceImple(AdminCompanyDAO dao) {
	    this.dao = dao;
	  }

	  @Override
	  public java.util.List<com.dacoach.admin.company.model.AdminCompanyRowDTO> companyList() {
	    return dao.companyList();
	  }

	  @Override
	  public com.dacoach.admin.company.model.AdminCompanyRowDTO companyDetail(int usersIdx) {
	    return dao.companyDetail(usersIdx);
	  }

	  @Override
	  @Transactional
	  public void saveCompanyDetail(int usersIdx, String certStatus, String status, 
	                                 String suspendFrom, String suspendUntil, String reason) {

	      // 1. 서류 승인 상태 업데이트 (독립적)
	      if (certStatus != null && !certStatus.isBlank()) {
	          dao.upsertCertStatus(usersIdx, certStatus);
	      }

	      // 2. 유저 계정 상태 업데이트
	      if (status != null && !status.isBlank()) {
	          dao.updateUserStatus(usersIdx, status);
	          
	          // 3. 정지 관련 로직 처리
	          if ("SUSPENDED".equals(status)) {
	              // 정지 상태라면 정보 저장 (종료일 필수)
	              java.sql.Date until = null;
	              if (suspendUntil != null && !suspendUntil.isBlank()) {
	                  until = java.sql.Date.valueOf(suspendUntil);
	                  dao.upsertEmbeddedUser(usersIdx, until, reason);
	              }
	          } 
	          else if ("ACTIVE".equals(status)) {
	              // 사용 상태로 변경 시, 진행 중인 정지 데이터가 있다면 오늘 날짜로 마감
	              dao.terminateSuspension(usersIdx);
	          }
	      }
	  }

}