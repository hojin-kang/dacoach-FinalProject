package com.dacoach.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.*;
import com.dacoach.service.qna.QnaService;
import com.dacoach.model.likes.LikesClassDTO;
import com.dacoach.model.likes.LikesUserDTO;
import com.dacoach.model.qna.QnaDTO;
import com.dacoach.service.likes.LikesService;
import com.dacoach.service.mypage.MypageService;

import jakarta.servlet.http.HttpSession;

@Controller
public class MypageController {

	@Autowired
    private QnaService qnaService;
	
	@Autowired
	private MypageService mypageService;
	
	@Autowired
	private LikesService likesService;

	@GetMapping("/mypage")
	public ModelAndView mypageMain(HttpSession session) {
		ModelAndView mav = new ModelAndView();
		if(session.getAttribute("user_idx")==null || (Integer)session.getAttribute("user_idx")==0) {
			mav.setViewName("/needLogin");
			return mav;
		}
		int user_idx = (Integer)session.getAttribute("user_idx");
		
		String user_nickname = "";
		String user_rank = "";
		
		try {
			Map users_info = mypageService.getUserInfo(user_idx);
			if (users_info != null) {
			    user_nickname = (String) users_info.get("NICKNAME");
			    user_rank = (String) users_info.get("RANKNAME");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
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
	  public ModelAndView myHeart(HttpSession session) throws Exception {
	    ModelAndView mav = new ModelAndView();

	    Integer userIdx = (Integer) session.getAttribute("user_idx");
	    if (userIdx == null) {
	      mav.setViewName("redirect:/login");
	      return mav;
	    }

	    List<LikesUserDTO> likedCoaches = likesService.getLikedCoaches(userIdx);
	    List<LikesClassDTO> likedClasses = likesService.getLikedClasses(userIdx);

	    mav.addObject("likedCoaches", likedCoaches);
	    mav.addObject("likedClasses", likedClasses);

	    mav.addObject("likedCoachCount", likedCoaches == null ? 0 : likedCoaches.size());
	    mav.addObject("likedClassCount", likedClasses == null ? 0 : likedClasses.size());

	    mav.setViewName("coach/mypage/myHeart");
	    return mav;
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
		if(session.getAttribute("user_idx")==null || (Integer)session.getAttribute("user_idx")==0) {
			mav.setViewName("/needLogin");
			return mav;
		}
		int user_idx = (Integer)session.getAttribute("user_idx");
		try {
			mav.addObject("qnaList", qnaService.myQnaList(user_idx));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		mav.setViewName("/coach/mypage/myQna");
		
		return mav;
	}
	
	@GetMapping("/myQnaDetail")
	public ModelAndView myQnaDetail(@RequestParam(value="qna_idx", defaultValue = "0") Integer qna_idx, HttpSession session) {
	    ModelAndView mav = new ModelAndView();
	    if(session.getAttribute("user_idx")==null || (Integer)session.getAttribute("user_idx")==0) {
			mav.setViewName("/needLogin");
			return mav;
		}
	    int user_idx = (Integer) session.getAttribute("user_idx");
	    Map<String, Object> data = null;
		try {
			data = qnaService.myQnaDetailWithAnswer(qna_idx, user_idx);
		} catch (Exception e) {
			e.printStackTrace();
		}

	    mav.addObject("qna", data.get("qna"));
	    mav.addObject("answer", data.get("answer"));
	    mav.setViewName("/coach/mypage/myQnaDetail");
	    return mav;
	}
	
	@GetMapping("/myQnaForm")
	public String myQnaForm(HttpSession session) {
		if(session.getAttribute("user_idx")==null || (Integer)session.getAttribute("user_idx")==0) {
			return "/needLogin";
		}
		return "/coach/mypage/myQnaForm";
	}
	
	@PostMapping("/myQnaNew")
	public ModelAndView myQnaNew(QnaDTO qdto, HttpSession session) {
		ModelAndView mav = new ModelAndView();
		if(session.getAttribute("user_idx")==null || (Integer)session.getAttribute("user_idx")==0) {
			mav.setViewName("/needLogin");
			return mav;
		}
		qdto.setUser_idx((Integer) session.getAttribute("user_idx"));
		int result = 0;
		try {
			result = qnaService.myQnaNew(qdto);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (result > 0) {
	        mav.setViewName("redirect:/myQnaList");
	    } else {
	        mav.setViewName("/coach/mypage/myQnaForm");
	    }
		return mav;
	}
}
