package com.dacoach.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminCompanyController {

	@GetMapping("/companyList")
	public String companyList(Model model) {
	    return "admin/company/companyList"; 
	}
	
}
