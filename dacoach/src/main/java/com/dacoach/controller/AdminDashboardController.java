package com.dacoach.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.dacoach.model.users.UsersDTO;
import com.dacoach.service.admin.AdminService;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Controller
public class AdminDashboardController {
	
	@Autowired
	private AdminService adminService;
	
	@GetMapping("/admin")
	public String adminLoginForm(
	        @CookieValue(value = "savedAdminId", defaultValue = "") String savedId,
	        Model model) {
	    model.addAttribute("savedId", savedId);
	    return "admin/login";
	}
	
	@PostMapping("/admin/login")
	public String adminLogin(@RequestParam String login_id,
			@RequestParam String password,
			HttpSession session,
			RedirectAttributes rttr,
			HttpServletResponse response,
			@RequestParam(value = "rememberId", required = false) String rememberId) {
		
		Map<String, String> loginMap = new HashMap<>();
		loginMap.put("login_id", login_id);
		loginMap.put("password", password);
		
		try {
			UsersDTO dto = adminService.adminLogin(loginMap);
			
			if(dto != null) {
				session.setAttribute("loginAdmin", dto);
				session.setAttribute("user_idx", dto.getUser_idx());
				
				if (!"ADMIN".equals(dto.getUser_type())) {
	                session.invalidate();
	                rttr.addFlashAttribute("msg", "관리자 권한이 없습니다.");
	                return "redirect:/admin";
	            }
				Cookie cookie = new Cookie("savedAdminId", "");
	            if ("on".equals(rememberId)) {
	                cookie.setValue(login_id);
	                cookie.setMaxAge(60 * 60 * 24 * 30);
	            } else {
	                cookie.setMaxAge(0); 
	            }
	            cookie.setPath("/");
	            response.addCookie(cookie);
				
				return "redirect:/admin/index";
				
			}else {
				rttr.addFlashAttribute("msg", "아이디 또는 비밀번호가 일치하지 않습니다.");
	            return "redirect:/admin";
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			rttr.addFlashAttribute("msg", "서버 오류가 발생했습니다.");
	        return "redirect:/admin";
		}
	}
	
	@GetMapping("/admin/index")
	public String dashboard(Model model,
			HttpSession session) {
		
		if(session.getAttribute("loginAdmin")==null) {
			return "redirect:/admin";
		}
		
		int dailySales=0;
		int newUsersCount=0;
		int pendingReportCount=0;
		int inactiveCompanyCount=0;
		
		try {
			dailySales=adminService.dailySales();
			newUsersCount=adminService.newUsers();
			pendingReportCount=adminService.pendingReports();
			inactiveCompanyCount=adminService.inactiveCompany();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		model.addAttribute("dailySales",dailySales);
		model.addAttribute("newUsersCount",newUsersCount);
		model.addAttribute("pendingReportCount",pendingReportCount);
		model.addAttribute("inactiveCompanyCount",inactiveCompanyCount);
        model.addAttribute("contentPage", "admin/index"); // 보여줄 파일
        model.addAttribute("contentFragment", "statsContent"); // 보여줄 조각
        return "admin/dashboard"; 
    }
	
	@GetMapping("/admin/logout")
	public String adminLogout(HttpSession session,
			RedirectAttributes rttr) {
		session.invalidate();
		rttr.addFlashAttribute("msg", "로그아웃 되었습니다.");
		return "redirect:/admin";
	}
}
