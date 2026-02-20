package com.dacoach.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.dacoach.page.PageModule;
import com.dacoach.service.adminpolicy.AdminPolicyService;

import jakarta.servlet.http.HttpSession; 

@Controller
@RequestMapping("/admin/support")
public class AdminNoticeController {

    @Autowired
    AdminPolicyService service;
    
    @GetMapping("/noticepolicy")
    public String notice(
            @RequestParam(value = "cp", required = false, defaultValue = "1") int cp, 
            Model model, HttpSession session) {
       
        if (session.getAttribute("loginAdmin") == null) {
            return "redirect:/admin";
        }
        

        int listSize = 5; 
        int pageSize = 5; 

        int totalCnt = service.getNoticeAllCnt(); 
        
        int startRow = (cp - 1) * listSize + 1;
        int endRow = cp * listSize;

        Map<String, Object> map = new HashMap<>();
        map.put("startRow", startRow);
        map.put("endRow", endRow);

        List<Map<String, Object>> noticeList = service.getNoticeList(map);

        String pageStr = PageModule.makePage("/admin/support/noticepolicy", totalCnt, listSize, pageSize, cp);

        model.addAttribute("noticeList", noticeList);
        model.addAttribute("pageStr", pageStr); 
        model.addAttribute("cp", cp);
        
        model.addAttribute("contentPage", "admin/support/notice/noticeList");
        model.addAttribute("contentFragment", "noticeList");

        return "admin/dashboard";
    }
}