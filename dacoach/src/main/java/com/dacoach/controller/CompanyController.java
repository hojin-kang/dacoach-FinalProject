package com.dacoach.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.dacoach.model.users.UsersDTO;
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
	
	@PostMapping("company/join/loginInfoOK")
	public ModelAndView LoginInfoOk(UsersDTO dto) {
		try {
			int idx = companyService.joinOk(dto);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ModelAndView mav=new ModelAndView();
		mav.addObject("login_id",dto.getLogin_id());
		mav.setViewName("forward:/company/profile/companyInfo");
		return mav;
	}
	
	@RequestMapping("/company/profile/companyInfo")
	public ModelAndView companyInfo() {
		
		ModelAndView mav=new ModelAndView();
		int idx=0;
		try {
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		mav.setViewName("/company/profile/companyInfo");
		return mav;
	}
	
	@PostMapping("/company/profile/profileForm")
	@ResponseBody()
	public ModelAndView profileForm(String phone,
									String address,
									String email,
									Integer company_num,
									String cert_status,
									String cert_name,
									String cert_file,
									String login_id) {
		m.put("phone", phone);
		m.put("address",address);
		m.put("email", email);
		m.put("company_num", company_num);
		m.put("cert_status", cert_status);
		m.put("cert_name", cert_name);
		m.put("cert_file", cert_file);
		System.out.println(login_id);
		try {
			int user_idx=companyService.getUserIdx(login_id);
			m.put("user_idx", user_idx);
			System.out.println(m);
			int infoResult=companyService.companyInfo(m);
			int certResurt=companyService.insertcert(m);
			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
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
	
	
	@GetMapping("/api/company/getMinorRegion")
	public ResponseEntity<List<Map<String, Object>>> tst(Integer regionIdx) {
		List<Map<String, Object>> minorRegion=null;
		try {
			minorRegion = companyService.getRegionTag(regionIdx);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ResponseEntity<List<Map<String, Object>>> re=new ResponseEntity<List<Map<String, Object>>>(minorRegion,HttpStatus.OK);
		return re;
	}

	
}
