package com.dacoach.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.dacoach.admincompany.service.AdminCompanyService;
import com.dacoach.model.company.CompanyDTO;
import com.dacoach.model.users.UsersDTO;

@Controller
@RequestMapping("/admin")
public class AdminCompanyController {

    private final AdminCompanyService adminCompanyService;

    public AdminCompanyController(AdminCompanyService adminCompanyService) {
        this.adminCompanyService = adminCompanyService;
    }

//    @GetMapping("/companyList")
//    public String companyList(Model model) {
//        List<CompanyDTO> companyList = adminCompanyService.companyList();
//        model.addAttribute("companyList", companyList);
//        return "admin/company/companyList";
//    }

    @GetMapping("/companyList")
    public String companyList(Model model) {
    	List<CompanyDTO> companyList = new ArrayList<>();
    	companyList = adminCompanyService.companyList();
    	
    	model.addAttribute("companyList",companyList);
    	model.addAttribute("contentPage","admin/company/companyList");
    	model.addAttribute("contentFragment","contentPage");
		return "admin/dashboard";
    }
}
