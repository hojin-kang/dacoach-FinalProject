package com.dacoach.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminCompanyController {

	@GetMapping("/admin/companyList")
	public String companyList() {
	    return "admin/company/companyList"; 
	}
	
}
