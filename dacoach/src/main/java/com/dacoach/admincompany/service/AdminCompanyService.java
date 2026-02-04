package com.dacoach.admincompany.service;

import java.util.List;
import java.util.Map;

import com.dacoach.admin.company.model.AdminCompanyRowDTO;
import com.dacoach.model.company.CompanyDTO;
import com.dacoach.model.users.UsersDTO;

public interface AdminCompanyService {
	List<AdminCompanyRowDTO> companyList();
	  AdminCompanyRowDTO companyDetail(int usersIdx);

	  void saveCompanyDetail(int usersIdx,
	                         String certStatus,
	                         String status,
	                         String suspendFrom,
	                         String suspendUntil,
	                         String reason);
}
