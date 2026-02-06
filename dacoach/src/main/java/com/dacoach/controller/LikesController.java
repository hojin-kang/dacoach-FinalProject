package com.dacoach.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import java.util.*;

import com.dacoach.service.likes.LikesService;

import jakarta.servlet.http.HttpSession;

@Controller
public class LikesController {

	@Autowired
	private LikesService likeService;

	@GetMapping("/addLikesClass")
	public ModelAndView addLikesClass(@RequestParam("class_idx") Integer classIdx, HttpSession session) {
		ModelAndView mav = new ModelAndView();

		Integer userIdx = (Integer) session.getAttribute("user_idx");
		if (userIdx == null) {
			mav.setViewName("redirect:/login");
			return mav;
		}

		try {
			likeService.addLikesClass(classIdx, userIdx);
		} catch (Exception e) {
			e.printStackTrace();
		}

		mav.setViewName("redirect:/coach/classDetail?id=" + classIdx);
		return mav;
	}

	@GetMapping("/delLikesClass")
	public ModelAndView delLikesClass(@RequestParam("class_idx") Integer classIdx, HttpSession session) {
		ModelAndView mav = new ModelAndView();

		Integer userIdx = (Integer) session.getAttribute("user_idx");
		if (userIdx == null) {
			mav.setViewName("redirect:/login");
			return mav;
		}

		try {
			likeService.delLikesClass(classIdx, userIdx);
		} catch (Exception e) {
			e.printStackTrace();
		}

		mav.setViewName("redirect:/coach/classDetail?id=" + classIdx);
		return mav;
	}
	
	@PostMapping("/api/likes/class/delete")
	@ResponseBody
	public Map<String, Object> deleteLikedClass(@RequestParam("class_idx") Integer class_idx,
	                                           HttpSession session) {
	    Integer user_idx = (Integer) session.getAttribute("user_idx");
	    if (user_idx == null) {
	        return Map.of("ok", false, "code", 401);
	    }

	    int result=0;
		try {
			result = likeService.delLikesClass(class_idx, user_idx);
		} catch (Exception e) {
			e.printStackTrace();
		}
	    return Map.of("ok", true, "deleted", result);
	}
	
	@PostMapping("/api/likes/user/delete")
	@ResponseBody
	public Map<String, Object> deleteLikedUser(@RequestParam("liked_user_idx") Integer liked_user_idx,
	                                          HttpSession session) {
	    Integer user_idx = (Integer) session.getAttribute("user_idx");
	    if (user_idx == null) {
	        return Map.of("ok", false, "code", 401);
	    }

	    int result = 0;
		try {
			result = likeService.delLikesUser(liked_user_idx, user_idx);
		} catch (Exception e) {
			e.printStackTrace();
		}
	    return Map.of("ok", true, "deleted", result);
	}

}
