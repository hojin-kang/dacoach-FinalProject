package com.dacoach.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
@Controller
public class UserController {
	@GetMapping("/login")
	public String login() {
		return "users/login";
	}
	@GetMapping("/userType")
	public String userType() {
		return "users/userType";
	}
}
