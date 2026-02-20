package com.dacoach.controller;

import java.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.dacoach.page.PageModule;
import com.dacoach.service.adminpolicy.AdminPolicyService;


@Controller
@RequestMapping("/admin")
public class AdminNoticeController {

    @Autowired
    AdminPolicyService service;
    
    @GetMapping("/noticepolicy")
    public String notice(@RequestParam(value = "cp", required = false, defaultValue = "1") int cp,
    		Model model,@RequestParam(value="type", defaultValue = "서비스")String type) {
    	
    	int listSize=10;
    	int pageSize=5;
    	int totalCnt = service.getNoticeAllCnt(type);
    	int startRow = (cp-1)*listSize+1;
    	int endRow = cp * listSize;
    	List<Map<String,Object>> noticeList = service.getNoticeList(type,startRow,endRow);
    	String pageUrl = "noticepolicy?type=" + type;
    	String pageStr = PageModule.makePagewithParams(pageUrl, totalCnt, listSize, pageSize, cp);
    	
    	model.addAttribute("noticeList",noticeList);
    	model.addAttribute("pageStr", pageStr);
    	model.addAttribute("cp", cp);
    	model.addAttribute("type",type);
    	return "admin/support/notice/noticepolicy";
    }
}