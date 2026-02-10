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

import com.dacoach.model.admin.EmbeddedUserDTO;
import com.dacoach.model.qna.QnaDTO;
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
				}else if("ACTIVE".equals(status)) {		
					adminService.updateEnddateSuspended(user_idx);
					model.addAttribute("msg", "사용 상태로 변경되었습니다.");
				}else if("WARNING".equals(status)) {
					adminService.updateEnddateSuspended(user_idx);
					model.addAttribute("msg", "주의 상태로 변경되었습니다.");
				}else if("DANGER".equals(status)) {
					adminService.updateEnddateSuspended(user_idx);
					model.addAttribute("msg", "위험 상태로 변경되었습니다.");
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
			@RequestParam (required = false) String keyword_type) {
	    
		List<Map<String, Object>> keywordType = new ArrayList<>();
		List<String> keywordName = new ArrayList<>();
		List<Map<String, Object>> typeReview = new ArrayList<>();
		
		try {
			keywordType = adminService.getKeywordType();
			keywordName = adminService.getKeywordName(keyword_type);
			typeReview = adminService.getTypeReview(keyword_type);
			
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
			RedirectAttributes rttr) {
		
		Map<String, String> params = new HashMap<>();
		params.put("keyword_type", keyword_type);
		params.put("keyword_name", keyword_name);
		
		try {
			int result = adminService.insertKeyword(params);
			
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
			RedirectAttributes rttr) {
		
		try {
			int result = adminService.deleteKeyword(keyword_name);
			
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
	public String deleteReview(@RequestBody Map<String, Object> params) {
		
		String role = (String) params.get("role");
		int review_idx = Integer.parseInt(String.valueOf(params.get("review_idx")));
		
		try {
			
			if("COACH".equals(role)) {
				adminService.deleteReviewCoach(review_idx);
			}else if("CLASS".equals(role)) {
				adminService.deleteReviewClass(review_idx);
			}
			
			return "success";
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "error";
		}
		
	}
	
	@GetMapping("/support/notice")
	public String noticeList(Model model,
			@RequestParam(value="keyword", required=false) String keyword) {
		
		List<Map<String, Object>> noticeList = new ArrayList<>();
		
		try {
			noticeList = adminService.getNoticeList(keyword);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		model.addAttribute("noticeList", noticeList);
		model.addAttribute("keyword", keyword);
		model.addAttribute("contentPage", "admin/support/notice/noticeList");
		model.addAttribute("contentFragment", "noticeList");
		
		return "admin/dashboard";
	}
	
	@GetMapping("/support/notice/write")
	public String noticeWrite(Model model) {
		
		model.addAttribute("contentPage", "admin/support/notice/noticeWrite");
		model.addAttribute("contentFragment", "noticeForm");
		
		return "admin/dashboard";
	}
	
	@PostMapping("/support/notice/insert")
	public String noticeInsert(QnaDTO dto,
			RedirectAttributes rttr) {
		
		try {
			int result = adminService.insertNotice(dto);
			
			if(result > 0) {
				rttr.addFlashAttribute("msg", "공지사항이 등록되었습니다.");
			}else {
				rttr.addFlashAttribute("msg", "공지사항 등록에 실패했습니다.");
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			rttr.addFlashAttribute("msg", "시스템 오류가 발생했습니다: " + e.getMessage());
		}
		
		return "redirect:/admin/support/notice";
	}
	
	@GetMapping("/support/notice/content")
	public String noticeContent(Model model, @RequestParam int qna_idx) {
		
		QnaDTO dto = new QnaDTO();
		
		try {
			dto = adminService.getNoticeContent(qna_idx);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		model.addAttribute("content", dto);
		model.addAttribute("contentPage", "admin/support/notice/noticeContent");
		model.addAttribute("contentFragment", "noticeContent");
		return "admin/dashboard";
	}
	
	@GetMapping("/support/notice/noticeUpdateForm")
	public String noticeUpdateForm(Model model, @RequestParam int qna_idx) {
		
		QnaDTO dto = new QnaDTO();
		
		try {
			dto = adminService.getNoticeContent(qna_idx);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		model.addAttribute("content", dto);
		model.addAttribute("contentPage", "admin/support/notice/noticeWrite");
		model.addAttribute("contentFragment", "noticeForm");
		return "admin/dashboard";
	}
	
	@PostMapping("/support/notice/update")
	public String updateNotice(QnaDTO dto,
			RedirectAttributes rttr) {
		
		try {
			int result = adminService.updateNotice(dto);
			
			if(result > 0) {
				rttr.addFlashAttribute("msg", "공지사항이 수정되었습니다.");
				return "redirect:/admin/support/notice";
			}else {
				rttr.addFlashAttribute("msg", "공지사항 수정에 실패했습니다.");
				return "redirect:/admin/support/notice/noticeUpdateForm?qna_idx=" + dto.getQna_idx();
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			rttr.addFlashAttribute("msg", "시스템 오류가 발생했습니다: " + e.getMessage());
			return "redirect:/admin/support/notice/noticeUpdateForm?qna_idx=" + dto.getQna_idx();
		}
		
	}
	
	@PostMapping("/support/notice/delete")
	public String deleteNotice(@RequestParam int qna_idx,
			RedirectAttributes rttr) {
		
		try {
			int result = adminService.deleteNotice(qna_idx);
			
			if(result > 0) {
				rttr.addFlashAttribute("msg", "공지사항이 삭제되었습니다.");
			}else {
				rttr.addFlashAttribute("msg", "공지사항 삭제에 실패했습니다.");
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			rttr.addFlashAttribute("msg", "시스템 오류가 발생했습니다: " + e.getMessage());
		}
		
		return "redirect:/admin/support/notice";
	}
	
	
	
	
	
	@GetMapping("/support/qna")
	public String qnaList(Model model) {
		
		model.addAttribute("contentPage", "admin/support/qna/qnaList");
		model.addAttribute("contentFragment", "qnaList");
		
		return "admin/dashboard";
	}
	
	@GetMapping("/filter/field")
	public String fieldContent(Model model,
			@RequestParam(required = false, defaultValue = "1") Integer major_field_idx) {

		List<Map<String, Object>> majorField = new ArrayList<>();
		List<Map<String, Object>> minorField = new ArrayList<>();
		String selectedMajorName = "";

		try {
			majorField = adminService.getMajorField();
			minorField = adminService.getMinorField(major_field_idx);

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
			RedirectAttributes rttr) {
		
		Map<String, Object> params = new HashMap<>();
		params.put("major_field_idx", major_field_idx);
		params.put("minor_field_nm", minor_field_nm);
		
		try {
			int result = adminService.insertMinorField(params);
			
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
	public String updateMinorField(@RequestBody Map<String, Object> params) {
		
		try {
			int result = adminService.updateMinorField(params);
			
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
