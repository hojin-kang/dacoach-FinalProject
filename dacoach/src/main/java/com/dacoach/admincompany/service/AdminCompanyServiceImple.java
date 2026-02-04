package com.dacoach.admincompany.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dacoach.admin.company.model.AdminCompanyDAO;
import com.dacoach.admin.company.model.AdminCompanyRowDTO;
import com.dacoach.model.company.CompanyDTO;

@Service
public class AdminCompanyServiceImple implements AdminCompanyService {

    private final AdminCompanyDAO adminCompanyDAO;

    public AdminCompanyServiceImple(AdminCompanyDAO adminCompanyDAO) {
        this.adminCompanyDAO = adminCompanyDAO;
    }

    @Override
    public List<AdminCompanyRowDTO> companyList() {
        return adminCompanyDAO.companyList();
    }

	@Override
	public AdminCompanyRowDTO companyDetail(int usersIdx) {
		return adminCompanyDAO.companyDetail(usersIdx);
	}
	
	@Transactional
    @Override
    public void saveCompanyDetail(Map<String, Object> param) {
        // 1) 계정 상태 저장
        adminCompanyDAO.updateUsersStatus(param);

        // 2) 정지 기간 저장(있으면 update, 없으면 insert) - status가 SUSPENDED일 때만 넣는 게 자연스러움
        String status = (String) param.get("status");
        if ("SUSPENDED".equals(status)) {
            adminCompanyDAO.upsertSuspendSetting(param);
        } else {
            // 정지가 아니면 정지기간은 비워두고 싶으면: start/end null로 업데이트(또는 별도 delete 쿼리)
            param.put("suspendFrom", "");
            param.put("suspendUntil", "");
            param.put("reason", "");
            adminCompanyDAO.upsertSuspendSetting(param);
        }

        // 3) 승인여부 저장(approve 버튼 눌렀으면 certStatus가 "확인"으로 들어오게 만들기)
        String certStatus = (String) param.get("certStatus");
        if (certStatus != null && !certStatus.trim().isEmpty()) {
            adminCompanyDAO.updateCertStatus(param);
        }
    }

}
