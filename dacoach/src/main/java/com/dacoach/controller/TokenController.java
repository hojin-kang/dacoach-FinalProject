package com.dacoach.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TokenController {

	@GetMapping("/tokenHistory")
	public String tokenHistory() {
		return "/coach/token/tokenHistory";
	}
}
