package com.dacoach.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.dacoach.admin.company.model.AdminCompanyRowDTO;
import com.dacoach.admincompany.service.AdminCompanyService;
import com.dacoach.page.PageModule;

@Controller
@RequestMapping("/admin")
public class AdminCompanyController {

  private final AdminCompanyService service;

  public AdminCompanyController(AdminCompanyService service) {
    this.service = service;
  }
  
  @GetMapping("/companyList")
  public String companyList(Model model) {
    model.addAttribute("list", service.companyList());
    model.addAttribute("contentPage", "admin/company/companyList");
    model.addAttribute("contentFragment", "contentPage");
    return "admin/dashboard";	
  }

  @GetMapping("/companyDetail/{usersIdx}")
  public String companyDetail(@PathVariable int usersIdx, Model model) {
    model.addAttribute("row", service.companyDetail(usersIdx));
    model.addAttribute("contentPage", "admin/company/companyDetail");
    model.addAttribute("contentFragment", "contentPage");
    return "admin/dashboard";
  }

  // 저장 버튼
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

  // 승인 버튼(승인여부만)
  @PostMapping("/companyDetail/{usersIdx}/approve")
  @ResponseBody
  public String approve(@PathVariable long usersIdx) {
    service.approveCompany(usersIdx);
    return "OK";
  }
  
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

      List<AdminCompanyRowDTO> list = service.getClassPage(map);

      String pageStr = PageModule.makePage("/admin/companyClassList", totalCnt, listSize, pageSize, cp);

      model.addAttribute("list", list);
      model.addAttribute("pageStr", pageStr);
      model.addAttribute("cp", cp);

      // dashboard에 끼우기
      model.addAttribute("contentPage", "admin/company/companyClassList");
      model.addAttribute("contentFragment", "contentPage");

      return "admin/dashboard";
  }

  	
}
