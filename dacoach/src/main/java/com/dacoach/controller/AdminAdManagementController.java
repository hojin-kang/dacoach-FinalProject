package com.dacoach.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.dacoach.adminad.service.AdminAdManagementService;
import com.dacoach.page.PageModule;

import jakarta.servlet.http.HttpSession;

import java.util.*;

@Controller
@RequestMapping("/admin")
public class AdminAdManagementController {

	@Autowired
	AdminAdManagementService adminAdService;

	@GetMapping("/adView")
	public String adView(
	        @RequestParam(value = "cp", required = false, defaultValue = "1") int cp,
	        Model model, HttpSession session
	) {
	    // 1. 세션 체크 (관리자 권한 확인)
	    if (session.getAttribute("loginAdmin") == null) {
	        return "redirect:/admin";
	    }

	    // 2. 페이징 설정
	    int listSize = 10; // 한 번에 보여줄 게시글 수
	    int pageSize = 5;  // 하단에 표시할 페이지 번호 개수

	    // 3. 전체 개수 조회 (요청하신 getAdTotalCnt 사용)
	    int totalCnt = adminAdService.getAdTotalCnt();

	    // 4. 페이지 범위 계산
	    int startRow = (cp - 1) * listSize + 1;
	    int endRow = cp * listSize;

	    Map<String, Object> map = new HashMap<>();
	    map.put("startRow", startRow);
	    map.put("endRow", endRow);

	    // 5. 페이징된 목록 조회 (서비스 메서드명을 페이징용으로 변경하거나 오버로딩 필요)
	    List<Map<String, Object>> adList = adminAdService.getAdRequestList(map);

	    // 6. 페이지네이션 HTML 생성
	    String pageStr = PageModule.makePage("/admin/adView", totalCnt, listSize, pageSize, cp);

	    // 7. 모델에 데이터 바인딩
	    model.addAttribute("adList", adList);
	    model.addAttribute("pageStr", pageStr); // 하단 페이지 번호
	    model.addAttribute("cp", cp);           // 현재 페이지 번호
	    
	    model.addAttribute("contentPage", "admin/ad/adManagement");
	    model.addAttribute("contentFragment", "contentPage");

	    return "admin/dashboard";
	}

	@GetMapping("/ad/process")
	public String approveAd(@RequestParam int ad_idx, @RequestParam int user_idx, @RequestParam String action,
			RedirectAttributes rttr, HttpSession session) {

		if(session.getAttribute("loginAdmin")==null) {
			return "redirect:/admin";
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
