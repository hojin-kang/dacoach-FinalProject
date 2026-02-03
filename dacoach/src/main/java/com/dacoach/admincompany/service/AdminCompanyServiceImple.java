package com.dacoach.admincompany.service;

import java.util.List;

import org.springframework.stereotype.Service;

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
	public CompanyDTO companyDetail(int usersIdx) {
		return adminCompanyDAO.companyDetail(usersIdx);
	}
}
