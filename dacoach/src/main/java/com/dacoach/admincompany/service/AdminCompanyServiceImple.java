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
	  public void saveCompanyDetail(int usersIdx,
	                                String certStatus,
	                                String status,
	                                String suspendFrom,
	                                String suspendUntil,
	                                String reason) {

	    // 1) CERT 승인상태 저장 (없으면 insert, 있으면 update)
	    String saveCertStatus = (certStatus == null || certStatus.isBlank()) ? "대기" : certStatus;
	    dao.upsertCertStatus(usersIdx, saveCertStatus);

	    // 2) USERS STATUS 저장 (ACTIVE/SUSPENDED)
	    // 값이 안 오면 건드리지 않음
	    if (status != null && !status.isBlank()) {
	      dao.updateUserStatus(usersIdx, status);
	    }

	    // 3) 정지라면 EMBEDDED_USER 저장(선택)
	    // - 날짜/사유가 비어있으면 "정지 기록 저장"은 스킵 가능
	    if ("SUSPENDED".equals(status)) {
	      Date from = (suspendFrom == null || suspendFrom.isBlank()) ? null : Date.valueOf(suspendFrom);
	      Date until = (suspendUntil == null || suspendUntil.isBlank()) ? null : Date.valueOf(suspendUntil);

	      // 너가 말한대로 "사용 정지만"이면 날짜 없어도 정지 가능하게 하고 싶을 수 있음.
	      // → 그럼 from만 오늘로 넣거나, 그냥 reason만 저장도 가능.
	      // 여기서는 입력이 하나라도 있으면 upsert 하게 처리.
	      boolean hasAny = (from != null) || (until != null) || (reason != null && !reason.isBlank());
	      if (hasAny) {
	        dao.upsertEmbeddedUser(usersIdx, from, until, reason);
	      }
	    } else if ("ACTIVE".equals(status)) {
	      // 사용으로 돌리면 정지 테이블 기록 지우고 싶으면 아래 사용
	      // dao.deleteEmbeddedUser(usersIdx);
	    }
	  }
}