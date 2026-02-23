package com.dacoach.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
import com.dacoach.service.adminCoach.AdminCoachService;
import com.dacoach.service.adminRefund.AdminRefundService;
import com.dacoach.service.adminad.AdminAdManagementService;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Controller
public class AdminDashboardController {
	
	@Autowired
	private AdminCoachService adminService;
	
	@Autowired
	private AdminRefundService adminRefundService;
	
	@Autowired
	private AdminAdManagementService adminAdManagementService;
	
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
		
		//To-Do List
		int reportCount=0;
		int certCount=0;
		int companyWaitCount=0;
		int refundCount=0;
		int adWaitCount=0;
		
		//최근 게시물 관리
		int keywordReviewCount=0;
		int qnaWaitCount=0;
		
		//운영팁
		List<Map<String, Object>> topInterests = new ArrayList<>();
		String tipMessage = "";
		
		try {
			dailySales=adminService.dailySales();
			newUsersCount=adminService.newUsers();
			pendingReportCount=adminService.pendingReports();
			inactiveCompanyCount=adminService.inactiveCompany();
			
			//To-Do List
			reportCount=adminService.reportCount();
			certCount=adminService.getCertCount();
			companyWaitCount=adminService.companyWaitCount();
			refundCount=adminRefundService.getRefundTotalCnt();
			adWaitCount=adminAdManagementService.getAdTotalCnt();
			
			//최근 게시물 관리
			keywordReviewCount=adminService.keywordReviewCount();
			qnaWaitCount=adminService.getQnaWaitCount();
			
			//운영팁
			topInterests=adminService.getTopCoachInterests();
			
			if(topInterests == null || topInterests.isEmpty()) {
				tipMessage = "현재 <b>코치 관심 키워드</b>를 집계 중입니다. 서비스 활성화를 위해 신규 코치 유치를 위한 집중 마케팅이 필요한 시점입니다.";
			}else {
				if(topInterests.size() == 1) {
					String field1 = String.valueOf(topInterests.get(0).get("MINOR_FIELD_NM"));
					tipMessage = "최근 코치들의 주요 관심 키워드는 <b>"+field1+"</b> 입니다. 해당 분야의 프로모션을 기획하기 좋은 시점입니다.";
				}else {
					String field1 = String.valueOf(topInterests.get(0).get("MINOR_FIELD_NM"));
					String field2 = String.valueOf(topInterests.get(1).get("MINOR_FIELD_NM"));
					tipMessage = "최근 코치들의 주요 관심 키워드는 <b>"+field1+"/"+field2+"</b> 입니다. 해당 분야의 프로모션을 기획하기 좋은 시점입니다.";
				}
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		model.addAttribute("dailySales",dailySales);
		model.addAttribute("newUsersCount",newUsersCount);
		model.addAttribute("pendingReportCount",pendingReportCount);
		model.addAttribute("inactiveCompanyCount",inactiveCompanyCount);
		
		//To-Do List
		model.addAttribute("reportCount",reportCount);
		model.addAttribute("certCount", certCount);
		model.addAttribute("companyWaitCount",companyWaitCount);
		model.addAttribute("refundCount",refundCount);
		model.addAttribute("adWaitCount",adWaitCount);
		
		//최근 게시물 관리
		model.addAttribute("keywordReviewCount",keywordReviewCount);
		model.addAttribute("qnaWaitCount",qnaWaitCount);
		
		//운영팁
		model.addAttribute("fullTipMessage", tipMessage);
		
        model.addAttribute("contentPage", "admin/index");
        model.addAttribute("contentFragment", "statsContent");
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
