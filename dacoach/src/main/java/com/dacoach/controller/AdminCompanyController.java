package com.dacoach.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.dacoach.admincompany.service.AdminCompanyService;
import com.dacoach.admin.company.model.AdminCompanyRowDTO;

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
	    return "admin/company/companyList";
	  }

	  @GetMapping("/companyDetail/{usersIdx}")
	  public String companyDetail(@PathVariable int usersIdx, Model model) {
	    AdminCompanyRowDTO row = service.companyDetail(usersIdx);
	    model.addAttribute("row", row);
	    return "admin/company/companyDetail";
	  }

	  @PostMapping("/companyDetail/save")
	  public String saveCompanyDetail(
	      @RequestParam("usersIdx") int usersIdx,
	      @RequestParam(value = "certStatus", required = false) String certStatus,
	      @RequestParam(value = "status", required = false) String status,
	      @RequestParam(value = "suspendFrom", required = false) String suspendFrom,
	      @RequestParam(value = "suspendUntil", required = false) String suspendUntil,
	      @RequestParam(value = "reason", required = false) String reason
	  ) {

	    // certStatus: 승인 버튼 누르면 '확인', 아니면 '' -> 서비스에서 '대기' 처리
	    // status: 라디오로 ACTIVE/SUSPENDED
	    service.saveCompanyDetail(usersIdx, certStatus, status, suspendFrom, suspendUntil, reason);

	    return "redirect:/admin/companyDetail/" + usersIdx;
	  }
}
