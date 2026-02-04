package com.dacoach.admin.company.model;

import java.util.List;
import java.util.Map;

import com.dacoach.model.company.CompanyDTO;
import com.dacoach.model.users.UsersDTO;

public interface AdminCompanyDAO {
	List<AdminCompanyRowDTO> companyList();
	AdminCompanyRowDTO companyDetail(int usersIdx);
	int updateUsersStatus(Map<String, Object> param);
	int upsertSuspendSetting(Map<String, Object> param);
	int updateCertStatus(Map<String, Object> param);
}
