package com.dacoach.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.dacoach.mapper.membership.MembershipMapper;
import com.dacoach.model.membership.MembershipDTO;
import com.dacoach.service.membership.MembershipService;

import jakarta.servlet.http.HttpSession;
import lombok.Getter;

@Controller
@RequestMapping("/membership")
public class MembershipController {
	
	@Autowired
	private MembershipService membershipService;
	private HttpSession session;
	private MembershipDTO dto;
	public MembershipController(HttpSession se) {
		session=se;
	}
	@GetMapping("/membershipForm")
	public ModelAndView membershipForm() {
		
		ModelAndView mav=new ModelAndView();
		
		
		try {
			dto=membershipService.userMembershipInfo((Integer)session.getAttribute("user_idx"));
			mav.addObject("session",session);
			mav.addObject("dto",dto);
			mav.setViewName("/membership/membershipForm");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return mav;
	}
	@GetMapping("/membershipUpForm")
	public ModelAndView membershipUpForm() {
		ModelAndView mav=new ModelAndView();
		
		try {
			mav.addObject("detail",membershipService.detailInfo(dto.getMember_detail_idx()));
			mav.addObject("session",session);
			mav.addObject("dto",dto);
			mav.setViewName("/membership/membershipUpdate");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return mav;
		
	}
	@GetMapping("/membershipDown")
	public ModelAndView membershipDown(MembershipDTO dto)	{
		ModelAndView mav=new ModelAndView();
					
		try {
			dto.setMember_detail_idx(1);
			dto.setUser_idx((Integer)session.getAttribute("user_idx"));
			membershipService.membershipDown(dto);
			mav.addObject("msg","멤버십 해지가 완료되었습니다");
			mav.addObject("url","/membership/membershipForm");
			mav.setViewName("/alert");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		return mav;
	}
}
