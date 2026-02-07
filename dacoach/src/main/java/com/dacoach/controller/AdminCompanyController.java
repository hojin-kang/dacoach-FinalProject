package com.dacoach.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.dacoach.admincompany.service.AdminCompanyService;
import com.dacoach.page.PageModule;

@Controller
@RequestMapping("/admin")
public class AdminCompanyController {

    private final AdminCompanyService service;

    public AdminCompanyController(AdminCompanyService service) {
        this.service = service;
    }

    // 1. 기업 목록
    @GetMapping("/companyList")
    public String companyList(Model model) {
        model.addAttribute("list", service.companyList());
        model.addAttribute("contentPage", "admin/company/companyList");
        model.addAttribute("contentFragment", "contentPage");
        return "admin/dashboard";
    }

    // 2. 기업 상세
    @GetMapping("/companyDetail/{usersIdx}")
    public String companyDetail(@PathVariable int usersIdx, Model model) {
        model.addAttribute("row", service.companyDetail(usersIdx));
        model.addAttribute("contentPage", "admin/company/companyDetail");
        model.addAttribute("contentFragment", "contentPage");
        return "admin/dashboard";
    }

    // 3. 저장 버튼
    @PostMapping("/companyDetail/save")
    public String saveCompanyDetail(
            @RequestParam("usersIdx") long usersIdx,
            @RequestParam("status") String status,
            @RequestParam(value="suspendFrom", required=false) String suspendFrom,
            @RequestParam(value="suspendUntil", required=false) String suspendUntil,
            @RequestParam(value="reason", required=false) String reason
    ) {
        service.saveAccountAndSuspend(usersIdx, status, suspendFrom, suspendUntil, reason);
        return "redirect:/admin/companyDetail/" + usersIdx;
    }

    // 4. 승인 버튼
    @PostMapping("/companyDetail/{usersIdx}/approve")
    @ResponseBody
    public String approve(@PathVariable long usersIdx) {
        service.approveCompany(usersIdx);
        return "OK";
    }

    // 5. 클래스 목록
    @GetMapping("/companyClassList")
    public String companyClassList(
            @RequestParam(value="cp", required=false, defaultValue="1") int cp,
            Model model
    ) {
        int listSize = 10;
        int pageSize = 5;

        int totalCnt = service.getClassTotalCnt();

        int startRow = (cp - 1) * listSize + 1;
        int endRow   = cp * listSize;

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
    
    //클래스 상세 페이지
    @GetMapping("/classDetail/{classIdx}")
    public String classDetail(@PathVariable("classIdx") int classIdx, Model model) {

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
    public String deleteReview(@PathVariable int classIdx,@PathVariable int reviewIdx) {
    	service.deleteReview(reviewIdx);
		return "ok";
    }
    


}