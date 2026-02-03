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
		return "/coach/mypage/mypage";
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
	public ModelAndView myQnaList() {
		ModelAndView mav = new ModelAndView();
		mav.addObject("qnaList", qnaService.myQnaList(1));
		mav.setViewName("/coach/mypage/myQna");
		
		return mav;
	}
	
	@GetMapping("qna/myQnaDetail")
	public String myQnaDetail() {
		return "/coach/mypage/myQnaDetail";
	}
}
