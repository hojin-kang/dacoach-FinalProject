package com.dacoach.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import com.dacoach.model.coach.CoachDTO;
import com.dacoach.model.users.UsersDTO;
import com.dacoach.service.coach.CoachService;
import com.dacoach.service.users.UsersService;

import jakarta.servlet.http.HttpSession;

@Controller
public class UserController {
	@Autowired
	private UsersService usersService;
	@Autowired
	private CoachService coachService;
	@GetMapping("/logout")
	public String logout(HttpSession session) {
		session.invalidate();
		return "redirect:/";
	}
	@GetMapping("/login")
	public String login() {
		return "users/login";
	}
	@GetMapping("/userType")
	public String userType() {
		return "users/userType";
	}
	@PostMapping("/loginOk")
	public ModelAndView loginOk(UsersDTO udto, HttpSession session) {
	    ModelAndView mav = new ModelAndView();

	    if (udto == null) {
	        System.out.println("로그인 객체가 생성되지 않았습니다.");
	        mav.setViewName("users/login");
	        return mav;
	    }

	    try {
	        UsersDTO loginUser = usersService.userLogin(udto);

	        if (loginUser == null) {
	            mav.setViewName("users/login");
	            return mav;
	        }
	        session.setAttribute("users_idx", loginUser.getUsers_idx());
	        
	        CoachDTO coachInfo = coachService.getCoachInfo(loginUser.getUsers_idx());
	        if (coachInfo != null && coachInfo.getPhoto() != null) {
	            session.setAttribute("photo", coachInfo.getPhoto());
	        } else {
	            session.setAttribute("photo", "default_profile.png");
	        }

	        mav.setViewName("redirect:/");

	    } catch (Exception e) {
	        e.printStackTrace();
	        mav.setViewName("users/login");
	    }
	    return mav;
	}
}
