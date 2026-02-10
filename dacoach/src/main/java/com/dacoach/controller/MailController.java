package com.dacoach.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.dacoach.service.mail.MailService;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/mail")
@RequiredArgsConstructor
public class MailController {

    private final MailService mailService;
    private final HttpSession session;

    @PostMapping("/send")
    @ResponseBody
    public String sendMail(@RequestParam String email, HttpSession session) {
        // 1. 6자리 랜덤 인증번호 생성
        String authCode = String.valueOf((int)(Math.random() * 899999) + 100000);
        
        // 2. 세션에 인증번호 저장
        session.setAttribute("authCode", authCode);
        // 세션 유지 시간을 3분(180초)으로 설정
        session.setMaxInactiveInterval(180); 

        // 3. 메일 발송 (우리가 만든 메서드는 제목/내용을 내부에서 처리하므로 email과 authCode만 전달)
        mailService.sendVerificationEmail(email, authCode);
        
        return "success";
    }
    @PostMapping("/verify")
	@ResponseBody
	public boolean verifyCode(@RequestParam String code, HttpSession session) {
	    String sessionCode = (String) session.getAttribute("authCode");
	    if (sessionCode != null && sessionCode.equals(code)) {
	        session.removeAttribute("authCode");
	        return true;
	    }
	    return false;
	}
    @PostMapping("/getId")
    @ResponseBody
    public Map<String, Object> getId(@RequestParam String code, @RequestParam String email, HttpSession session) {
        Map<String, Object> response = new HashMap<>();
        String sessionCode = (String) session.getAttribute("authCode");

        if (sessionCode != null && sessionCode.equals(code)) {
            session.removeAttribute("authCode");
            String userId = mailService.getId(email);
            
            if (userId == null) {
                response.put("status", "none");
            } else {
                response.put("status", "success");
                response.put("userId", userId);
            }
        } else {
            response.put("status", "fail");
        }
        return response;
    }
    @PostMapping("/getCompanyId")
    @ResponseBody
    public Map<String, Object> getCompanyId(@RequestParam String code, @RequestParam String email, HttpSession session) {
        Map<String, Object> response = new HashMap<>();
        String sessionCode = (String) session.getAttribute("authCode");

        if (sessionCode != null && sessionCode.equals(code)) {
            session.removeAttribute("authCode");
            String userId = mailService.getCompanyId(email);
            
            if (userId == null) {
                response.put("status", "none");
            } else {
                response.put("status", "success");
                response.put("userId", userId);
            }
        } else {
            response.put("status", "fail");
        }
        return response;
    }
}
