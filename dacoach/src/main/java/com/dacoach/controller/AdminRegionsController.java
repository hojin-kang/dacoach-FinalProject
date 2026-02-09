package com.dacoach.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
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
	@ResponseBody
	public List<Map<String, Object>> getMajorRegions() {
		return service.getMajorRegions();
	}
	
}
