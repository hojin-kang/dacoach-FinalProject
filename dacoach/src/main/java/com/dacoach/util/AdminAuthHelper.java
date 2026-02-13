package com.dacoach.util;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.dacoach.model.users.UsersDTO;

import jakarta.servlet.http.HttpSession;

@Component
public class AdminAuthHelper {

	public String checkAdminAuth(HttpSession session, RedirectAttributes rttr) {
		UsersDTO admin = (UsersDTO) session.getAttribute("loginAdmin");
		System.out.println("userDTO=" + admin.toString());
		if (admin == null) {
			rttr.addFlashAttribute("msg", "로그인이 필요한 서비스입니다.");
			return "redirect:/admin";
		}

		if (!"ADMIN".equals(admin.getUser_type())) {
			session.invalidate();
			rttr.addFlashAttribute("msg", "접근 권한이 없습니다.");
			return "redirect:/admin";
		}
		return null;
	}

}
