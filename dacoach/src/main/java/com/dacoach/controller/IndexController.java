package com.dacoach.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import com.dacoach.model.coach.CoachDTO;
import com.dacoach.model.company.AdDTO;
import com.dacoach.service.coach.CoachService;
import com.dacoach.service.membership.MembershipService;

@Controller
public class IndexController {
	
	@Autowired
	private MembershipService membershipService;
	
	@Autowired
	private CoachService coachService;
	
	@GetMapping("/")
	public ModelAndView index() {
		ModelAndView mav=new ModelAndView();
		List<AdDTO> lists=null;
		List<CoachDTO> cdtos = null;
		
		try {
			lists=membershipService.bannerSelect(null);
			cdtos = coachService.getPopularCoach();
			mav.addObject("list",lists);
			mav.addObject("popularCoaches", cdtos);
		} catch (Exception e) {
			e.printStackTrace();
		}
		mav.setViewName("index");
		return mav;
	}
}
