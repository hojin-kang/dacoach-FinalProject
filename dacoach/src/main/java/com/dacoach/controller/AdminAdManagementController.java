package com.dacoach.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminAdManagementController {

	@GetMapping("/adView")
	public String adView(Model model) {
		model.addAttribute("contentPage","admin/ad/adManagement");
		model.addAttribute("contentFragment","contentPage");
		return "admin/dashboard";
	}
}
