package com.dacoach.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class NotificationController {

	@GetMapping("/notification")
	public String notification() {
		return "/coach/notification";
	}
}
