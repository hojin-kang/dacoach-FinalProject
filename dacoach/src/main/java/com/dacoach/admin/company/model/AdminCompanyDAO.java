package com.dacoach.admin.company.model;

import java.sql.Date;
import java.util.List;
import java.util.Map;

import com.dacoach.model.company.CompanyDTO;
import com.dacoach.model.users.UsersDTO;

public interface AdminCompanyDAO {
	List<AdminCompanyRowDTO> companyList();
	  AdminCompanyRowDTO companyDetail(int usersIdx);

	  int upsertCertStatus(int usersIdx, String certStatus);

	  int updateUserStatus(int usersIdx, String status);

	  int upsertEmbeddedUser(int usersIdx, Date startDate, Date endDate, String reason);
	
}
