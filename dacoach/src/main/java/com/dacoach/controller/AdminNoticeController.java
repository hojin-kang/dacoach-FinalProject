package com.dacoach.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.dacoach.adminpolicy.service.AdminPolicyService;
import com.dacoach.model.adminPolicy.PolicyDTO; 

@Controller
@RequestMapping("/admin")
public class AdminNoticeController {

	@Autowired
	AdminPolicyService service;
	
    @GetMapping("/notice")
    public String noticeList() {
        return "admin/support/notice/policies";  
    }
    
    @GetMapping("/termsofuse")
    public String termsOfUse() {
		return "admin/support/notice/termsOfUse";  
	}
    
    @GetMapping("/privacypolicy")
    public String privacyPolicy() {
    	return "admin/support/notice/privacyPolicy";
    }
    
    @GetMapping("/adMarketing")
    public String adMarketing() {
		return "admin/support/notice/adMarketing";
	}
    
    @GetMapping("/businessInformation")
    public String businessInformation() {
    	return "admin/support/notice/businessInformation";
    }
    
    @GetMapping("/noticepolicy")
    public String notice(Model model) {
    	List<PolicyDTO> noticeList = service.getNoticeList();
    	model.addAttribute("noticeList",noticeList);
		return "admin/support/notice/noticePolicy";
	}
    
    
}