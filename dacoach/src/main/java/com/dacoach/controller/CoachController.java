package com.dacoach.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.dacoach.service.coach.CoachService;

@Controller
public class CoachController {
	@Autowired
	private CoachService coachService;
	
	@GetMapping("/coachJoin")
	public String coachJoinForm() {
		return "coach/coachJoin";
	}
	
	@GetMapping("/api/member/idCheck")
    @ResponseBody
    public boolean checkId(@RequestParam("username") String username) {
		boolean result=true;
        try {
        	result=coachService.idCheck(username);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return !result;
    }
	
	
}
