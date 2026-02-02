package com.dacoach.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import com.dacoach.service.qna.QnaService;

@Controller
public class MypageController {

	@Autowired
    private QnaService qnaService;

	@GetMapping("/mypage")
	public String mypageMain() {
		return "/mypage/mypage";
	}
	
	@GetMapping("/myInfo")
	public String myInfo() {
		return "/mypage/myInfo";
	}
	
	@GetMapping("/myHeart")
	public String myHeartList() {
		return "/mypage/myHeart";
	}
	
	@GetMapping("/myReview")
	public String myReviewList() {
		return "/mypage/myReview";
	}
	
	
	
	@GetMapping("/myPayment")
	public String myPaymentList() {
		return "/mypage/myPayment";
	}
	
	@GetMapping("/notice")
	public String noticeList() {
		return "/mypage/notice";
	}
	
	@GetMapping("/myQnaList")
	public ModelAndView myQnaList() {
		ModelAndView mav = new ModelAndView();
		mav.addObject("qnaList", qnaService.myQnaList(1));
		mav.setViewName("/mypage/myQna");
		
		return mav;
	}
}
