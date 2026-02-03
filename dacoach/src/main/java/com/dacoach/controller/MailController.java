package com.dacoach.controller;

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
    public String sendMail(@RequestParam String email) {
        // 1. 6자리 랜덤 인증번호 생성
        String authCode = String.valueOf((int)(Math.random() * 899999) + 100000);
        
        // 2. 세션에 인증번호 저장 (3분 동안 유효하게 설정 가능)
        session.setAttribute("authCode", authCode);
        session.setMaxInactiveInterval(180); 

        // 3. 메일 발송
        mailService.sendEmail(email, "다코치 인증번호입니다.", "인증번호: " + authCode);
        
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
}
