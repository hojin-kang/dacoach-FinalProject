package com.dacoach.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.dacoach.admin.company.model.AdminCompanyRowDTO;
import com.dacoach.admincompany.service.AdminCompanyService;

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
    	List<AdminCompanyRowDTO> companyList = new ArrayList<>();
    	companyList = adminCompanyService.companyList();
    	
    	model.addAttribute("companyList",companyList);
    	model.addAttribute("contentPage","admin/company/companyList");
    	model.addAttribute("contentFragment","contentPage");
		return "admin/dashboard";
    }
    
    @GetMapping("/companyDetail/{usersIdx}")
    public String companyDetail(@PathVariable int usersIdx, Model model) {

        AdminCompanyRowDTO row = adminCompanyService.companyDetail(usersIdx); // 한 건 조회

        model.addAttribute("contentPage", "admin/company/companyDetail");
        model.addAttribute("contentFragment", "contentPage");
        model.addAttribute("row", row); // 이거 필수

        return "admin/dashboard";
    }


}
