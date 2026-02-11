package com.dacoach.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class FooterConroller {

	@GetMapping("/howItWorks")
	public String howItWorks() {
		return "/howItWorks";
	}
}
