package com.dacoach.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import com.dacoach.model.coach.CoachDTO;
import com.dacoach.model.coachClasses.CoachClassDTO;
import com.dacoach.model.company.AdDTO;
import com.dacoach.model.minorField.MinorFieldDTO;
import com.dacoach.service.coach.CoachService;
import com.dacoach.service.coachClasses.CoachClassService;
import com.dacoach.service.membership.MembershipService;

@Controller
public class IndexController {
	
	@Autowired
	private MembershipService membershipService;
	
	@Autowired
	private CoachService coachService;
	
	@Autowired
	private CoachClassService coachClassService;
	
	@GetMapping("/")
	public ModelAndView index() {
		ModelAndView mav=new ModelAndView();
		List<Map<String, Object>> majorFields = null;
		List<AdDTO> lists=null;
		List<CoachDTO> cdtos = null;
		List<MinorFieldDTO> mdtos = null;
		List<CoachClassDTO> ccdtos = null;
		
		try {
			majorFields = coachService.getMajorFields();
			lists=membershipService.bannerSelect(null);
			cdtos = coachService.getPopularCoach();
			mdtos = coachService.getPopularField();
			ccdtos = coachClassService.getPopularClass();
			
			mav.addObject("majorFields",majorFields);
			mav.addObject("list",lists);
			mav.addObject("popularCoaches", cdtos);
			mav.addObject("popularFields", mdtos);
			mav.addObject("popularClasses", ccdtos);
		} catch (Exception e) {
			e.printStackTrace();
		}
		mav.setViewName("index");
		return mav;
	}
}
