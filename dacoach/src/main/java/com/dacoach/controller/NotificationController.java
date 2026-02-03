package com.dacoach.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import com.dacoach.model.notification.NotificationDTO;
import com.dacoach.service.notification.NotificationService;

import jakarta.servlet.http.HttpSession;


@Controller
public class NotificationController {
	
	@Autowired
    private NotificationService nService;

	@GetMapping("/notification")
	public ModelAndView notification(HttpSession session) {
		ModelAndView mav = new ModelAndView();
		int users_idx = (Integer)session.getAttribute("users_idx");
		List<NotificationDTO> ndtos = null;
		try {
			ndtos = nService.notiList(users_idx);
		} catch (Exception e) {
			e.printStackTrace();
		}
		mav.addObject("ndtos", ndtos);
		mav.setViewName("/coach/notification");
		return mav;
	}
}
