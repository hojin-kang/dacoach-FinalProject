package com.dacoach.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/coach")
public class AdminCoachController {

	
	@GetMapping("/coachList")
    public String coachList(Model model) {
        model.addAttribute("contentPage", "admin/coach/coachList");
        model.addAttribute("contentFragment", "coachContent");
        
        //model.addAttribute("userList", coachService.getUserList());
        return "admin/dashboard";
    }
}
