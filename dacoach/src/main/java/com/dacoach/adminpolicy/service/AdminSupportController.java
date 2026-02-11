package com.dacoach.adminpolicy.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.dacoach.model.adminPolicy.PolicyDTO;

@Controller
@RequestMapping("/support")
public class AdminSupportController {

	@Autowired
	AdminPolicyService service;
	
	@GetMapping("/policy")
	public String viewOperatingPolicy(Model model) {
		PolicyDTO latestPolicy = service.getLatestPolicy();
		model.addAttribute("latestPolicy", latestPolicy);
		return "admin/support/notice/policies";
	}
}