package com.dacoach.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;
import java.util.*;

import com.dacoach.model.token.TokenHistoryDTO;
import com.dacoach.service.token.TokenService;

import jakarta.servlet.http.HttpSession;

@Controller
public class TokenController {

	@Autowired
	private TokenService tokenService;
	
	@GetMapping("/tokenHistory")
	public ModelAndView tokenHistory(HttpSession session) {
		
		ModelAndView mav = new ModelAndView();
		
		Integer user_idx = (Integer) session.getAttribute("user_idx");
        if (user_idx == null || user_idx == 0) {
        	mav.setViewName("redirect:/needLogin");
        	return mav;
        }
        
        Integer token_balance = 0;
        List<TokenHistoryDTO> thdtos = null;
        
        try {
        	token_balance = tokenService.getMyToken(user_idx);
        	thdtos = tokenService.getTokenHistory(user_idx);
        	mav.addObject("token_balance", token_balance);
        	mav.addObject("thdto", thdtos);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		mav.setViewName("coach/token/tokenHistory");
		
		return mav;
	}
	
	@GetMapping("/tokenChargeForm")
	public ModelAndView tokenChargeForm(HttpSession session) {
		ModelAndView mav = new ModelAndView();
		
		Integer user_idx = (Integer) session.getAttribute("user_idx");
        if (user_idx == null || user_idx == 0) {
        	mav.setViewName("redirect:/needLogin");
        	return mav;
        }
        
        mav.setViewName("coach/token/tokenChargeForm");
		
		return mav;
	}
	
}
