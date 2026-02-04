package com.dacoach.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

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

        AdminCompanyRowDTO row = adminCompanyService.companyDetail(usersIdx); 

        model.addAttribute("contentPage", "admin/company/companyDetail");
        model.addAttribute("contentFragment", "contentPage");
        model.addAttribute("row", row); 

        return "admin/dashboard";
    }

    @PostMapping("/companyDetail/save")
    public String saveCompanyDetail(
            @RequestParam int usersIdx,
            @RequestParam String status,
            @RequestParam(required = false) String suspendFrom,
            @RequestParam(required = false) String suspendUntil,
            @RequestParam(required = false) String reason,
            @RequestParam(required = false) String certStatus
    ) {
        Map<String, Object> param = new HashMap<>();
        param.put("usersIdx", usersIdx);
        param.put("status", status);

        param.put("suspendFrom", suspendFrom == null ? "" : suspendFrom);
        param.put("suspendUntil", suspendUntil == null ? "" : suspendUntil);
        param.put("reason", reason == null ? "" : reason);

        param.put("certStatus", certStatus == null ? "" : certStatus);

        adminCompanyService.saveCompanyDetail(param);

        return "redirect:/admin/companyDetail/" + usersIdx;
    }
}
