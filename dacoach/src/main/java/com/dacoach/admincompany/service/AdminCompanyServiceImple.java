package com.dacoach.admincompany.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.dacoach.admin.company.model.AdminCompanyDAO;
import com.dacoach.model.company.CompanyDTO;
import com.dacoach.model.users.UsersDTO;

@Service
public class AdminCompanyServiceImple implements AdminCompanyService {

    private final AdminCompanyDAO adminCompanyDAO;

    public AdminCompanyServiceImple(AdminCompanyDAO adminCompanyDAO) {
        this.adminCompanyDAO = adminCompanyDAO;
    }

    @Override
    public List<CompanyDTO> companyList() {
        return adminCompanyDAO.companyList();
    }
}
