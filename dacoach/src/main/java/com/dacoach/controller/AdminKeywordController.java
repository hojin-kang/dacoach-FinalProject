package com.dacoach.controller;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.dacoach.service.adminKeyword.KeywordService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/admin")
public class AdminKeywordController {

	@Autowired
	private KeywordService keywordService;
	
	//키워드관리
	@GetMapping("/keyword/keyword")
	public String keyword(Model model,
			@RequestParam (required = false) String keyword_type,
			HttpSession session) {
	    
		if(session.getAttribute("loginAdmin")==null) {
			return "redirect:/admin";
		}
		
		List<Map<String, Object>> keywordType = new ArrayList<>();
		List<String> keywordName = new ArrayList<>();
		List<Map<String, Object>> typeReview = new ArrayList<>();
		
		try {
			keywordType = keywordService.getKeywordType();
			keywordName = keywordService.getKeywordName(keyword_type);
			typeReview = keywordService.getTypeReview(keyword_type);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		model.addAttribute("keywordType", keywordType);
		model.addAttribute("keywordName", keywordName);
		model.addAttribute("typeReview", typeReview);
	    model.addAttribute("contentPage", "admin/keyword/keyword");
	    model.addAttribute("contentFragment", "keywordContent");
	    
	    return "admin/dashboard";
	}
	
	@PostMapping("/keyword/insertKeyword")
	public String insertKeyword(@RequestParam String keyword_type,
			@RequestParam String keyword_name,
			RedirectAttributes rttr,
			HttpSession session) {
		
		if(session.getAttribute("loginAdmin")==null) {
			return "redirect:/admin";
		}
		
		Map<String, String> params = new HashMap<>();
		params.put("keyword_type", keyword_type);
		params.put("keyword_name", keyword_name);
		
		try {
			int result = keywordService.insertKeyword(params);
			
			if(result > 0) {
				rttr.addFlashAttribute("msg", "키워드가 추가되었습니다.");
			}else {
				rttr.addFlashAttribute("msg", "키워드 추가에 실패했습니다.");
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			rttr.addFlashAttribute("msg", "시스템 오류가 발생했습니다: " + e.getMessage());
		}
		
		rttr.addAttribute("keyword_type", keyword_type);

		return "redirect:/admin/keyword/keyword";

		
	}
	
	@GetMapping("/keyword/deleteKeyword")
	public String deleteKeyword(@RequestParam String keyword_name,
			RedirectAttributes rttr, HttpSession session) {
		
		if(session.getAttribute("loginAdmin")==null) {
			return "redirect:/admin";
		}
		
		try {
			int result = keywordService.deleteKeyword(keyword_name);
			
			if(result > 0) {
				rttr.addFlashAttribute("msg", "키워드가 삭제되었습니다.");
			}else {
				rttr.addFlashAttribute("msg", "키워드 삭제에 실패했습니다.");
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			rttr.addFlashAttribute("msg", "시스템 오류가 발생했습니다: " + e.getMessage());
		}
		
		return "redirect:/admin/keyword/keyword";
	}

	@PostMapping("/keyword/deleteReview")
	@ResponseBody
	public String deleteReview(@RequestBody Map<String, Object> params,
			HttpSession session) {
		
		if(session.getAttribute("loginAdmin")==null) {
			return "redirect:/admin";
		}
		
		String role = (String) params.get("role");
		int review_idx = Integer.parseInt(String.valueOf(params.get("review_idx")));
		
		try {
			
			if("COACH".equals(role)) {
				keywordService.deleteReviewCoach(review_idx);
			}else if("CLASS".equals(role)) {
				keywordService.deleteReviewClass(review_idx);
			}
			
			return "success";
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "error";
		}
		
	}
	
	
	//분야필터관리
	@GetMapping("/filter/field")
	public String fieldContent(Model model,
			@RequestParam(required = false, defaultValue = "1") Integer major_field_idx,
			HttpSession session) {

		if(session.getAttribute("loginAdmin")==null) {
			return "redirect:/admin";
		}
		
		List<Map<String, Object>> majorField = new ArrayList<>();
		List<Map<String, Object>> minorField = new ArrayList<>();
		String selectedMajorName = "";

		try {
			majorField = keywordService.getMajorField();
			minorField = keywordService.getMinorField(major_field_idx);

			for (Map<String, Object> map : majorField) {
				if (map.get("MAJOR_FIELD_IDX").toString().equals(major_field_idx.toString())) {
					selectedMajorName = (String) map.get("MAJOR_FIELD_CD");
					break;
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		model.addAttribute("majorField", majorField);
		model.addAttribute("minorField", minorField);

		model.addAttribute("selectedMajorIdx", major_field_idx);
		model.addAttribute("selectedMajorName", selectedMajorName);

		model.addAttribute("contentPage", "admin/filter/fieldFilters");
		model.addAttribute("contentFragment", "fieldContent");

		return "admin/dashboard";
	}
	
	@PostMapping("/filter/insert")
	public String insertMinorField(
			@RequestParam int major_field_idx,
			@RequestParam String minor_field_nm,
			RedirectAttributes rttr,
			HttpSession session) {
		
		if(session.getAttribute("loginAdmin")==null) {
			return "redirect:/admin";
		}
		
		Map<String, Object> params = new HashMap<>();
		params.put("major_field_idx", major_field_idx);
		params.put("minor_field_nm", minor_field_nm);
		
		try {
			int result = keywordService.insertMinorField(params);
			
			if(result > 0) {
				rttr.addFlashAttribute("msg", "세부 분야가 추가되었습니다.");
			}else {
				rttr.addFlashAttribute("msg", "세부 분야 추가에 실패했습니다.");
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			rttr.addFlashAttribute("msg", "시스템 오류 발생");
		}
		
		return "redirect:/admin/filter/field?major_field_idx=" + major_field_idx;
	}
	
	@PostMapping("/filter/update")
	@ResponseBody
	public String updateMinorField(@RequestBody Map<String, Object> params,
			HttpSession session) {
		
		if(session.getAttribute("loginAdmin")==null) {
			return "redirect:/admin";
		}
		
		try {
			int result = keywordService.updateMinorField(params);
			
			if(result > 0) {
				return "success";
			}else {
				return "fail";
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "error";
		}
	}
	
}
