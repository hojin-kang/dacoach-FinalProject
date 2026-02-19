package com.dacoach.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.dacoach.adminpolicy.service.AdminPolicyService;
import com.dacoach.model.adminPolicy.PolicyDTO;
import com.dacoach.model.users.UsersDTO;
import com.dacoach.page.PageModule;
import com.dacoach.service.admin.AdminService;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/admin/support")
public class AdminPolicyController {

	@Autowired
	AdminPolicyService service;

	@Autowired
	AdminService adminService;

	@GetMapping("/policy")
	public String viewPolicyPage(@RequestParam(value = "cp", required = false, defaultValue = "1") int cp, Model model,
			HttpSession session) {

		if (session.getAttribute("loginAdmin") == null) {
			return "redirect:/admin";
		}

		int listSize = 5;
		int pageSize = 5;
		int totalCnt = service.getNoticeTotalCnt();
		int startRow = (cp - 1) * listSize + 1;
		int endRow = cp * listSize;

		Map<String, Object> map = new HashMap<>();
		map.put("startRow", startRow);
		map.put("endRow", endRow);
		List<Map<String, Object>> policyList = service.getPolicyList(map);

		String pageStr = PageModule.makePage("/admin/support/policy", totalCnt, listSize, pageSize, cp);

		model.addAttribute("policyList", policyList);
		model.addAttribute("pageStr", pageStr);
		model.addAttribute("cp", cp);
		model.addAttribute("contentPage", "/admin/support/policy/operating");
		model.addAttribute("contentFragment", "contentPage");

		return "admin/dashboard";
	}

	@PostMapping("/policy/save")
	public String savePolicy(PolicyDTO policy, HttpSession session) {
		UsersDTO loginUser = (UsersDTO) session.getAttribute("loginAdmin");

		if (loginUser == null) {
			return "redirect:/admin";
		}

		policy.setUser_idx(loginUser.getUser_idx());
		service.savePolicy(policy);

		return "redirect:/admin/support/policy";
	}

	@GetMapping("/policy/delete")
	public String deletePolicy(@RequestParam int qna_idx) {
		service.deletePolicy(qna_idx);
		return "redirect:/admin/support/policy";
	}
}