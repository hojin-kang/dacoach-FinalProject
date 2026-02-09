package com.dacoach.controller;

import org.springframework.stereotype.Controller; 
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminNoticeController {

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
    public String notice() {
		return "admin/support/notice/noticePolicy";
	}
}