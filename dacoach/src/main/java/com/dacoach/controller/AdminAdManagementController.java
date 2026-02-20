package com.dacoach.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.dacoach.adminad.service.AdminAdManagementService;
import com.dacoach.page.PageModule;

import jakarta.servlet.http.HttpSession;
import java.util.*;

@Controller
@RequestMapping("/admin")
public class AdminAdManagementController {

    @Autowired
    AdminAdManagementService adminAdService;

    @GetMapping("/adView")
    public String adView(
            @RequestParam(value = "cp", required = false, defaultValue = "1") int cp,
            Model model, HttpSession session) {
        
        if (session.getAttribute("loginAdmin") == null) return "redirect:/admin";

        int listSize = 10; 
        int pageSize = 5;  

        int totalCnt = adminAdService.getAdTotalCnt();
        
        int startRow = (cp - 1) * listSize + 1;
        int endRow = cp * listSize;

       List<Map<String,Object>> adList = adminAdService.getAdRequestList(startRow, endRow);

        String pageStr = PageModule.makePage("adView", totalCnt, listSize, pageSize, cp);

        model.addAttribute("adList", adList);
        model.addAttribute("pageStr", pageStr);
        model.addAttribute("cp", cp);           
        
        model.addAttribute("contentPage", "admin/ad/adManagement");
        model.addAttribute("contentFragment", "contentPage");

        return "admin/dashboard";
    }

    @GetMapping("/ad/process")
    public String approveAd(@RequestParam int ad_idx, @RequestParam int user_idx, @RequestParam String action,
            RedirectAttributes rttr, HttpSession session) {

        if(session.getAttribute("loginAdmin") == null) return "redirect:/admin";
        
        int result = adminAdService.sendNotification(ad_idx, user_idx, action);
        if (result > 0) {
            String msg = "APPROVE".equals(action) ? "승인 처리 완료" : "반려 처리 완료";
            rttr.addFlashAttribute("msg", msg);
        }

        // [수정] 처리 후 1페이지로 안전하게 리다이렉트
        return "redirect:/admin/adView?cp=1";
    }
}