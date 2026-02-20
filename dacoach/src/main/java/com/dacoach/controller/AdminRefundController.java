package com.dacoach.controller;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.dacoach.page.PageModule;
import com.dacoach.service.adminRefund.AdminRefundService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/admin")
public class AdminRefundController {
	
	@Autowired
	private AdminRefundService adminRefundService;
	
	@GetMapping("/refund")
	public String refundList(@RequestParam(value = "cp", required = false, defaultValue = "1") int cp, Model model,
			HttpSession session) {
		if(session.getAttribute("loginAdmin")==null) {
			return "redirect:/admin";
		}
		
		int listSize = 10;
		int pageSize = 5;
		int totalCnt = adminRefundService.getRefundTotalCnt();
		int start = (cp - 1) * listSize + 1;
		int end = cp * listSize;
		
		List<Map<String,Object>> list = adminRefundService.getRefundList(start,end);
		String pageStr = PageModule.makePage("refund", totalCnt, listSize, pageSize, cp);
		model.addAttribute("pageStr", pageStr);
		model.addAttribute("refund_list",list);
		model.addAttribute("contentPage","admin/revenue/refundList");
		model.addAttribute("contentFragment","contentPage");
		
		return "admin/dashboard";
	}
	
	@PostMapping("/refund/approve")
    @ResponseBody
    public ResponseEntity<String> approve_refund(@RequestParam("pay_idx") int pay_idx) {
        String result = adminRefundService.approveRefund(pay_idx);
        
        if ("SUCCESS".equals(result)) {
            return ResponseEntity.ok("정상적으로 환불되었습니다.");
        } else if ("NOT_FOUND".equals(result)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("결제 정보를 찾을 수 없습니다.");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("환불 처리에 실패했습니다.");
        }
    }
}
