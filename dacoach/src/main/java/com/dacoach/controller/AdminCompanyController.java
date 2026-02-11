package com.dacoach.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.dacoach.admincompany.service.AdminCompanyService;
import com.dacoach.page.PageModule;

@Controller
@RequestMapping("/admin")
public class AdminCompanyController {

    @Autowired
    private AdminCompanyService service;
    
    //기업 회원 목록
    @GetMapping("/companyList")
    public String companyList(Model model) {
        List<Map<String, Object>> list = service.getCompanyList();
        model.addAttribute("list", list);
        model.addAttribute("contentPage", "admin/company/companyList");
        model.addAttribute("contentFragment", "contentPage");
        return "admin/dashboard";
    }

    //기업 회원 상세
    @GetMapping("/companyDetail/{usersIdx}")
    public String companyDetail(@PathVariable long usersIdx, Model model) {
        Map<String, Object> row = service.getCompanyDetail(usersIdx);
        String certFilePath = service.getCertFilePath((int) usersIdx);
        model.addAttribute("certFilePath", certFilePath);
        model.addAttribute("row", row);	
        model.addAttribute("contentPage", "admin/company/companyDetail");
        model.addAttribute("contentFragment", "contentPage");
        return "admin/dashboard";
    }

    //기업 회원 상태 변경 (저장)
    @PostMapping("/companyDetail/save")
    public String saveCompanyStatus(
            @RequestParam long usersIdx,
            @RequestParam String status,
            @RequestParam(required = false) String suspendFrom,
            @RequestParam(required = false) String suspendUntil,
            @RequestParam(required = false) String reason
    ) {
        service.updateCompanyStatus(usersIdx, status, suspendFrom, suspendUntil, reason);
        return "redirect:/admin/companyDetail/" + usersIdx;
    }

    //승인 처리
    @PostMapping("/companyDetail/{usersIdx}/approve")
    @ResponseBody
    public String approveCompany(@PathVariable long usersIdx) {
        service.approveCert(usersIdx);
        return "OK";
    }

    //클래스 목록
    @GetMapping("/companyClassList")
    public String companyClassList(
            @RequestParam(value = "cp", required = false, defaultValue = "1") int cp,
            Model model
    ) {
        int listSize = 10;
        int pageSize = 5;

        int totalCnt = service.getClassTotalCnt();

        int startRow = (cp - 1) * listSize + 1;
        int endRow = cp * listSize;

        Map<String, Object> map = new HashMap<>();
        map.put("startRow", startRow);
        map.put("endRow", endRow);

        List<Map<String, Object>> list = service.getClassPage(map);

        String pageStr = PageModule.makePage("/admin/companyClassList", totalCnt, listSize, pageSize, cp);

        model.addAttribute("list", list);
        model.addAttribute("pageStr", pageStr);
        model.addAttribute("cp", cp);

        model.addAttribute("contentPage", "admin/company/companyClassList");
        model.addAttribute("contentFragment", "contentPage");

        return "admin/dashboard";
    }
    
    //클래스 상세
    @GetMapping("/classDetail/{classIdx}")
    public String classDetail(@PathVariable int classIdx, Model model) {
        Map<String, Object> classInfo = service.getClassDetail(classIdx);
        List<Map<String, Object>> reviews = service.getClassReviews(classIdx);
        
        model.addAttribute("classInfo", classInfo);
        model.addAttribute("reviews", reviews);
        model.addAttribute("contentPage", "admin/company/classDetail");
        model.addAttribute("contentFragment", "contentPage");
        return "admin/dashboard";
    }

    //리뷰 삭제
    @PostMapping("/classDetail/{classIdx}/deleteReview/{reviewIdx}")
    @ResponseBody
    public String deleteReview(@PathVariable int classIdx, @PathVariable int reviewIdx) {
        service.deleteReview(reviewIdx);
        return "OK";
    }
}