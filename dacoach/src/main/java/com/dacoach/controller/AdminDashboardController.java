package com.dacoach.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminDashboardController {
	
	@GetMapping("/admin/mainStats")
	public String dashboard(Model model) {
        model.addAttribute("contentPage", "admin/mainStats"); // 보여줄 파일
        model.addAttribute("contentFragment", "statsContent"); // 보여줄 조각
        return "admin/dashboard"; 
    }
}
