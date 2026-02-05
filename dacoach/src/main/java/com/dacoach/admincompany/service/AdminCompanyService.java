// ================================
// 2) Service 인터페이스
// ================================
package com.dacoach.admincompany.service;

import java.util.List;
import com.dacoach.admin.company.model.AdminCompanyRowDTO;

public interface AdminCompanyService {

    // 목록/상세
    List<AdminCompanyRowDTO> companyList();
    AdminCompanyRowDTO companyDetail(int usersIdx);

    // 저장 버튼: 계정상태 + 정지이력 저장(정지는 누적, 사용으로 바꾸면 최신 이력 enddate=sysdate)
    void saveAccountAndSuspend(long userIdx, String status,
                               String suspendFrom, String suspendUntil, String reason);

    // 승인 버튼: 승인여부(CERT)만 확인 처리(USERS 상태는 변경하지 않음)
    void approveCompany(long userIdx);
}
