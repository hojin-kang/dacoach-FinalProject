package com.dacoach.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.dacoach.service.company.CompanyService;

@Controller
public class CompanyController {
	
	@Autowired
	private CompanyService companyService;
	
	private HashMap<String,Object> m;
	
	public CompanyController() {
		m=new HashMap<String,Object>();
	}
	
	@GetMapping("/companyJoin")
	public String joinForm() {

		return "/company/join/companyJoin";
	}
	@GetMapping("/company/profile/companyInfo")
	public ModelAndView companyInfo(String login_id,
									String password,
									String user_name,
									String user_type,
									String status){
		m.put("login_id", login_id);
		m.put("password", password);
		m.put("user_name", user_name);
		m.put("user_type", user_type);
		m.put("status", status);

		
		ModelAndView mav=new ModelAndView();
		
		mav.setViewName("/company/profile/companyInfo");
		return mav;
	}
	
	@GetMapping("/company/profile/profileForm")
	public ModelAndView profileForm(String phone,
									String address,
									String email,
									String company_num,
									String cert_status,
									String cert_name,
									String cert_file) {
		m.put("phone", phone);
		m.put("address",address);
		m.put("email", email);
		m.put("company_num", company_num);
		m.put("cert_status", cert_status);
		m.put("cert_name", cert_name);
		m.put("cert_file", cert_file);
		System.out.println(m);
		ModelAndView mav=new ModelAndView();
		List<Map<String,Object>> field=null;
		List<Map<String,Object>> region=null;
		try {
			field=companyService.fieldTeg();
			region=companyService.regionTeg();
			mav.addObject("field_con",field);
			mav.addObject("region_con",region);
		}catch (Exception e) {
			e.printStackTrace();
		}
		mav.setViewName("/company/profile/profileForm");
		return mav;
	}
}
