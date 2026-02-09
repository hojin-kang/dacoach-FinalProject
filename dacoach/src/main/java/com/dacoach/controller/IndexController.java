package com.dacoach.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import com.dacoach.model.company.AdDTO;
import com.dacoach.service.membership.MembershipService;

@Controller
public class IndexController {
	
	@Autowired
	private MembershipService membershipService;
	
	@GetMapping("/")
	public ModelAndView index() {
		ModelAndView mav=new ModelAndView();
		List<AdDTO> lists=null;
		
		try {
			lists=membershipService.bannerSelect(null);
			mav.addObject("list",lists);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		mav.setViewName("index");
		return mav;
	}
}
