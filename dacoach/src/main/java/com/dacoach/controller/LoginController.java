package com.dacoach.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {
	@GetMapping("/login")
	public String login() {
		return "login/login";
	}
	@GetMapping("/memberType")
	public String memberType() {
		return "login/memberType";
	}
	@GetMapping("/coachJoin")
	public String coachJoin() {
		return "coach/coachJoin";
	}
}
