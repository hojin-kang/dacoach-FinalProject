package com.dacoach.admincompany.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dacoach.admin.company.model.AdminCompanyDAO;

@Service
public class AdminCompanyServiceImple implements AdminCompanyService {

    private final AdminCompanyDAO dao;

    public AdminCompanyServiceImple(AdminCompanyDAO dao) {
        this.dao = dao;
    }

    @Override
    public List<Map<String, Object>> companyList() {
        return dao.companyList();
    }

    @Override
    public Map<String, Object> companyDetail(int usersIdx) {
        return dao.companyDetail(usersIdx);
    }

    @Override
    @Transactional
    public void saveAccountAndSuspend(long userIdx, String status,
                                      String suspendFrom, String suspendUntil, String reason) {

        // 1) 계정상태 저장
        if (status != null && !status.isBlank()) {
            dao.updateUserStatus(userIdx, status);
        }

        // 2) 정지면: 정지이력 누적 INSERT
        if ("SUSPENDED".equals(status)) {
            // 시작일이 비어있으면 오늘로
            if (suspendFrom == null || suspendFrom.isBlank()) {
                suspendFrom = LocalDate.now().format(DateTimeFormatter.ISO_DATE);
            }
            dao.insertEmbeddedHistory(userIdx, suspendFrom, suspendUntil, reason);
            return;
        }

        // 3) 사용으로 바꾸면: 최신 정지이력 1건 END_DATE = SYSDATE로 종료
        if ("ACTIVE".equals(status)) {
            dao.closeLatestEmbeddedHistory(userIdx);
        }
    }

    @Override
    @Transactional
    public void approveCompany(long userIdx) {
        // 1) 승인 여부(CERT) 처리
        String certType = "사업증";
        String certStatus = "확인";

        int cnt = dao.countCertByUserAndType(userIdx, certType);
        if (cnt > 0) {
            dao.updateCertStatus(userIdx, certStatus);
        } else {
            dao.insertCertStatus(userIdx, certStatus);
        }

        // 2) 계정 상태(USERS)를 'ACTIVE'로 변경
        dao.updateUserStatus(userIdx, "ACTIVE");

        // 3) 만약 정지 중인 이력이 있다면 종료 처리
        dao.closeLatestEmbeddedHistory(userIdx);
    }

    @Override
    public List<Map<String, Object>> getClassPage(Map<String, Object> param) {
        return dao.selectClassPage(param);
    }

    @Override
    public int getClassTotalCnt() {
        return dao.countClassTotal();
    }

	@Override
	public Map<String, Object> getClassDetail(int classIdx) {
		return dao.classDetail(classIdx);
	}

	@Override
	public List<Map<String, Object>> getClassReviews(int classIdx) {
		return dao.selectClassReviews(classIdx);
	}

	@Override
	public void deleteReview(int reviewIdx) {
		dao.deleteReview(reviewIdx);
	}
}