package com.dacoach.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.dacoach.adminad.service.AdminAdManagementService;
import com.dacoach.util.AdminAuthHelper;

import jakarta.servlet.http.HttpSession;

import java.util.*;

@Controller
@RequestMapping("/admin")
public class AdminAdManagementController {

	@Autowired
	AdminAdManagementService adminAdService;

	@Autowired
	AdminAuthHelper adminAuthHelper;

	@GetMapping("/adView")
	public String adView(Model model, HttpSession session, RedirectAttributes rttr) {

		String redirect = adminAuthHelper.checkAdminAuth(session, rttr);

		if (redirect != null) {
			return redirect;
		}

		List<Map<String, Object>> adList = adminAdService.getAdRequestList();
		model.addAttribute("adList", adList);
		model.addAttribute("contentPage", "admin/ad/adManagement");
		model.addAttribute("contentFragment", "contentPage");

		return "admin/dashboard";
	}

	@GetMapping("/ad/process")
	public String approveAd(@RequestParam int ad_idx, @RequestParam int user_idx, @RequestParam String action,
			RedirectAttributes rttr, HttpSession session) {

		String redirect = adminAuthHelper.checkAdminAuth(session, rttr);

		if (redirect != null) {
			return redirect;
		}
		int result = adminAdService.sendNotification(ad_idx, user_idx, action);
		String msg = null;
		if (result > 0) {
			msg = "APPROVE".equals(action) ? "승인 처리 완료" : "반려 처리 완료";
			rttr.addFlashAttribute("msg", msg);
		}

		return "redirect:/admin/adView";
	}
}
