package com.dacoach.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.dacoach.adminRegions.service.AdminRegionsService;

@Controller
@RequestMapping("/admin/filter")
public class AdminRegionsController {

	@Autowired
	private AdminRegionsService service;
	
	@GetMapping("/regions")
	public String filterRegions() {
		return "admin/filter/regionalFilters";  
	}
	
	@GetMapping("/major")
	public String getMajorRegions(Model model) {	
		List<Map<String, Object>> majorRegions = service.getMajorRegions();
		model.addAttribute("majorRegions", majorRegions);
		model.addAttribute("contentPage", "admin/filter/regionalFilters");
		model.addAttribute("contentFragment", "contentPage");
		return "admin/dashboard";
	}
	
}
