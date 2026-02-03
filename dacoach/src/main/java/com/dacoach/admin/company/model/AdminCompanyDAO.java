package com.dacoach.admin.company.model;

import java.util.List;

import com.dacoach.model.company.CompanyDTO;
import com.dacoach.model.users.UsersDTO;

public interface AdminCompanyDAO {
    List<CompanyDTO> companyList();
}
