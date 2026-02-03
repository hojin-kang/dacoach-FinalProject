package com.dacoach.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.*;
import com.dacoach.service.qna.QnaService;
import com.dacoach.service.mypage.MypageService;

import jakarta.servlet.http.HttpSession;

@Controller
public class MypageController {

	@Autowired
    private QnaService qnaService;
	
	@Autowired
	private MypageService mypageService;

	@GetMapping("/mypage")
	public ModelAndView mypageMain(HttpSession session) {
		ModelAndView mav = new ModelAndView();
		int users_idx = (Integer)session.getAttribute("users_idx");
		System.out.println(users_idx);
		Map users_info = mypageService.getUserInfo(users_idx);
		String user_nickname = (String)users_info.get("NICKNAME");
		String user_rank = (String)users_info.get("RANKNAME");
		System.out.println(user_nickname);
		System.out.println(user_rank);
		mav.addObject("user_nickname", user_nickname);
		mav.addObject("user_rank", user_rank);
		mav.setViewName("/coach/mypage/mypage");
		return mav;
	}
	
	@GetMapping("/myInfo")
	public String myInfo() {
		return "/coach/mypage/myInfo";
	}
	
	@GetMapping("/myHeart")
	public String myHeartList() {
		return "/coach/mypage/myHeart";
	}
	
	@GetMapping("/myReview")
	public String myReviewList() {
		return "/coach/mypage/myReview";
	}
	
	
	
	@GetMapping("/myPayment")
	public String myPaymentList() {
		return "/coach/mypage/myPayment";
	}
	
	@GetMapping("/notice")
	public String noticeList() {
		return "/coach/mypage/notice";
	}
	
	@GetMapping("/myQnaList")
	public ModelAndView myQnaList(HttpSession session) {
		ModelAndView mav = new ModelAndView();
		int users_idx = (Integer)session.getAttribute("users_idx");
		mav.addObject("qnaList", qnaService.myQnaList(users_idx));
		mav.setViewName("/coach/mypage/myQna");
		
		return mav;
	}
	
	@GetMapping("qna/myQnaDetail")
	public String myQnaDetail() {
		return "/coach/mypage/myQnaDetail";
	}
}
