package com.dacoach.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import com.dacoach.service.company.CompanyService;

@Controller
public class CompanyController {
	
	@Autowired
	private CompanyService companyService;
	
	@GetMapping("/companyJoin")
	public String joinForm() {
		return "/company/join/companyJoin";
	}
	@GetMapping("/company/profile/companyInfo")
	public ModelAndView companyInfo(){
		
		ModelAndView mav=new ModelAndView();
		mav.setViewName("/company/profile/companyInfo");
		return mav;
	}
	
	@GetMapping("/company/profile/profileForm")
	public ModelAndView profileForm() {
		
		ModelAndView mav=new ModelAndView();
		mav.setViewName("/company/profile/profileForm");
		return mav;
	}
}
