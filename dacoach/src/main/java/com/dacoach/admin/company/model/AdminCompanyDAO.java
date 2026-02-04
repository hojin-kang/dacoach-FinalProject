package com.dacoach.admin.company.model;

import java.sql.Date;
import java.util.List;

public interface AdminCompanyDAO {
	List<AdminCompanyRowDTO> companyList();
	AdminCompanyRowDTO companyDetail(int usersIdx);
	int upsertCertStatus(int usersIdx, String certStatus);
	int updateUserStatus(int usersIdx, String status);
	int upsertEmbeddedUser(int usersIdx, Date suspendUntil, String reason);
	int terminateSuspension(int usersIdx);
	
}
