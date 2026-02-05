package com.dacoach.controller;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.dacoach.model.admin.EmbeddedUserDTO;
import com.dacoach.service.admin.AdminService;

@Controller
@RequestMapping("/admin")
public class AdminCoachController {

	@Autowired
	private AdminService adminService;
	
	
	@GetMapping("/coach/coachList")
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
	
	
	@GetMapping("/coach/coachDetail")
	public String coachDetail(Model model,@RequestParam int coach_idx, @RequestParam int user_idx) {
		
		List<Map<String, Object>> coachDetail = new ArrayList<>();
		String status="";
		
		try {
			coachDetail = adminService.getCoachDetail(coach_idx);
			status = adminService.getCoachStatus(user_idx);
			
			model.addAttribute("coachStatus", status);
			
			if("SUSPENDED".equals(status)) {
				EmbeddedUserDTO dto = adminService.getCoachEmbedded(user_idx);
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
	
	@PostMapping("/coach/updateCoachStatus")
	public String updateCoachSuspended(Model model,
			@RequestParam int coach_idx,
			@RequestParam int user_idx,
			@RequestParam String status,
			EmbeddedUserDTO dto) {
		
		int updateresult = 0;
		int insertresult = 0;
		
		Map<String, Object> params = new HashMap<>();
		params.put("user_idx", user_idx);
	    params.put("status", status);
		
		try {
			updateresult = adminService.updateCoachStatus(params);
			
			if(updateresult > 0) {
				if("SUSPENDED".equals(status)) {
					dto.setUser_idx(user_idx);
					insertresult = adminService.insertCoachSuspended(dto);
					
					if(insertresult > 0) {
						model.addAttribute("msg", "계정이 정지 처리 되었습니다.");
					}
				}else {
					
					adminService.updateEnddateSuspended(user_idx);
					model.addAttribute("msg", "사용 상태로 변경되었습니다.");
				}
				
			}else {
				model.addAttribute("msg", "상태 변경에 실패했습니다.");
			}	
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			model.addAttribute("msg", "시스템 오류가 발생했습니다: " + e.getMessage());
		}
		
		model.addAttribute("contentPage", "admin/coach/coachDetail");
		model.addAttribute("contentFragment", "coachDetail");
	
		model.addAttribute("user_idx", user_idx);
	    model.addAttribute("coach_idx", coach_idx);
		
		return "admin/dashboard";
	}
	
	
	@GetMapping("/keyword/keyword")
	public String keyword(Model model,
			@RequestParam (required = false) String type) {
	    
		List<Map<String, Object>> keywordType = new ArrayList<>();
		try {
			keywordType = adminService.getKeywordType();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		model.addAttribute("keywordType", keywordType);
	    model.addAttribute("contentPage", "admin/keyword/keyword");
	    model.addAttribute("contentFragment", "keywordContent");
	    
	    return "admin/dashboard";
	}
	
	
}
