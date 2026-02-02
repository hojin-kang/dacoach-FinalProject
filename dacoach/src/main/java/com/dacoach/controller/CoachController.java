package com.dacoach.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class CoachController {
	@GetMapping("/coachJoin")
	public String coachJoinForm() {
		return "coach/coachJoin";
	}
	
	@GetMapping("/api/member/idCheck")
    @ResponseBody
    public boolean checkId(@RequestParam("username") String username) {
        
        return true;
    }
	
}
