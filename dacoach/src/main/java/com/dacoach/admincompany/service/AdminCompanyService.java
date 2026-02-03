package com.dacoach.admincompany.service;

import java.util.List;

import com.dacoach.admin.company.model.AdminCompanyRowDTO;
import com.dacoach.model.company.CompanyDTO;
import com.dacoach.model.users.UsersDTO;

public interface AdminCompanyService {
    List<AdminCompanyRowDTO> companyList();
    AdminCompanyRowDTO companyDetail(int usersIdx);
}
