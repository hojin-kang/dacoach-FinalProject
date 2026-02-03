package com.dacoach.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class CompanyController {
	
	@GetMapping("/companyJoin")
	public String joinForm() {
		return "/company/join/companyJoin";
	}
	
	@PostMapping("/company/profile/profileForm")
	public String profileForm() {
		return "/company/profile/profileForm";
	}
}
