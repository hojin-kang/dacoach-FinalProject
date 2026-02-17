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

import com.dacoach.service.adminRefund.AdminRefundService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/admin")
public class AdminRefundController {
	
	@Autowired
	private AdminRefundService adminRefundService;
	
	@GetMapping("/refund")
	public String refundList(Model model,HttpSession session) {
		if(session.getAttribute("loginAdmin")==null) {
			return "redirect:/admin";
		}
		List<Map<String,Object>> list = adminRefundService.getRefundList();
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
