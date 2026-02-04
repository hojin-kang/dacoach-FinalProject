package com.dacoach.controller;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.dacoach.model.admin.EmbeddedUserDTO;
import com.dacoach.service.admin.AdminService;

@Controller
@RequestMapping("/admin/coach")
public class AdminCoachController {

	@Autowired
	private AdminService adminService;
	
	
	@GetMapping("/coachList")
	public String coachList(Model model) {
	    List<Map<String, Object>> coachList = new ArrayList<>();
	    int certCount = 0;
	    
	    try {
	        coachList = adminService.getCoachList();
	        certCount = adminService.getCertCount();
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    
	    model.addAttribute("coachList", coachList);
	    model.addAttribute("certCount", certCount);
	    
	    model.addAttribute("contentPage", "admin/coach/coachList");
	    model.addAttribute("contentFragment", "coachContent");
	    
	    return "admin/dashboard";
	}
	
	
	@GetMapping("/coachDetail")
	public String coachDetail(Model model,@RequestParam("coach_idx") int coachidx, @RequestParam("user_idx") int useridx) {
		
		List<Map<String, Object>> coachDetail = new ArrayList<>();
		String status="";
		
		try {
			coachDetail = adminService.getCoachDetail(coachidx);
			status = adminService.getCoachStatus(useridx);
			
			model.addAttribute("coachStatus", status);
			
			if("SUSPENDED".equals(status)) {
				EmbeddedUserDTO dto = adminService.getCoachEmbedded(useridx);
				model.addAttribute("coachEmbedded", dto);
				
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		model.addAttribute("coachDetail", coachDetail);
		
		model.addAttribute("contentPage", "admin/coach/coachDetail");
		model.addAttribute("contentFragment", "coachDetail");
		
		return "admin/dashboard";
	}
	
	@PostMapping("/updateCoachStatus")
	public String updateCoachSuspended(Model model,
			@RequestParam("coachIdx") int coachidx,
			@RequestParam("userIdx") int useridx,
			EmbeddedUserDTO dto) {
		
		int updateresult = 0;
		int insertresult = 0;
		
		try {
			updateresult = adminService.updateCoachSuspended(useridx);
			insertresult = adminService.insertCoachSuspended(dto);
			
			if(updateresult > 0 && insertresult > 0) {
				model.addAttribute("msg", "정지 처리 되었습니다.");
			}else {
				model.addAttribute("msg", "정지 처리에 실패했습니다.");
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			model.addAttribute("msg", "시스템 오류가 발생했습니다: " + e.getMessage());
		}
		
		model.addAttribute("contentPage", "admin/coach/coachDetail");
		model.addAttribute("contentFragment", "coachDetail");
	
		model.addAttribute("userIdx", useridx);
	    model.addAttribute("coachIdx", coachidx);
		
		return "admin/dashboard";
	}
	
	
	
	
}
