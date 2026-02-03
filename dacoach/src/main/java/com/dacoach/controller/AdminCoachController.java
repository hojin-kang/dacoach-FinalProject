package com.dacoach.controller;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.dacoach.service.admin.AdminService;

@Controller
@RequestMapping("/admin/coach")
public class AdminCoachController {

	@Autowired
	private AdminService adminService;
	
	
	@GetMapping("/coachList")
	public String coachList(Model model) {
	    List<Map<String, Object>> coachList = new ArrayList<>();
	    int certCount = 0;
	    
	    try {
	        coachList = adminService.getCoachList();
	        certCount = adminService.getCertCount();
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    
	    model.addAttribute("coachList", coachList);
	    model.addAttribute("certCount", certCount);
	    
	    model.addAttribute("contentPage", "admin/coach/coachList");
	    model.addAttribute("contentFragment", "coachContent");
	    
	    return "admin/dashboard";
	}
	
	
	@GetMapping("/coachDetail")
	public String coachDetail(Model model,@RequestParam("coachIdx") int coachidx) {
		List<Map<String, Object>> coachDetail = new ArrayList<>();
		
		try {
			coachDetail = adminService.getCoachDetail(coachidx);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		model.addAttribute("coachDetail", coachDetail);
		
		model.addAttribute("contentPage", "admin/coach/coachDetail");
		model.addAttribute("contentFragment", "coachDetail");
		
		return "admin/dashboard";
	}
	
}
