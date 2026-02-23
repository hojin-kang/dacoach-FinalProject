package com.dacoach.controller;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.dacoach.model.admin.EmbeddedUserDTO;
import com.dacoach.model.coach.CoachDTO;
import com.dacoach.model.company.CertDTO;
import com.dacoach.service.adminCoach.AdminCoachService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/admin")
public class AdminCoachController {

	@Autowired
	private AdminCoachService adminCoachService;
	
	//개인고객관리(코치)
	@GetMapping("/coach/coachList")
	public String coachList(Model model, HttpSession session,
			@RequestParam(value="cp", defaultValue="1") int cp,
			@RequestParam(value="sortColumn", required=false) String sortColumn,
			@RequestParam(value="keyword", required=false) String keyword) {
		
		if(session.getAttribute("loginAdmin")==null) {
			return "redirect:/admin";
		}
		
		int listSize=10;
		int pageSize=5;
		int coachTotalCnt = 0;
	    List<Map<String, Object>> coachList = new ArrayList<>();
	    int certCount = 0;
	    
		try {
		    coachTotalCnt=adminCoachService.getCoachTotalCnt(keyword);
		    	
		    int start = (cp - 1) * listSize + 1;
		    int end = cp * listSize;
		    	
		    coachList = adminCoachService.getCoachList(keyword, sortColumn, start, end);
		    certCount = adminCoachService.getCertCount();
		} catch (Exception e) {
		    e.printStackTrace();
		}
		
		String pageStr = "";
	    if ((keyword != null && !keyword.isEmpty()) || (sortColumn != null && !sortColumn.isEmpty())) {
	        StringBuilder urlBuilder = new StringBuilder("coachList?");
	        if (keyword != null && !keyword.isEmpty()) {
	            urlBuilder.append("keyword=").append(keyword);
	        }
	        if (sortColumn != null && !sortColumn.isEmpty()) {
	            if (urlBuilder.length() > 10) urlBuilder.append("&");
	            urlBuilder.append("sortColumn=").append(sortColumn);
	        }
	        pageStr = com.dacoach.page.PageModule.makePagewithParams(urlBuilder.toString(), coachTotalCnt, listSize, pageSize, cp);
	    } else {
	        pageStr = com.dacoach.page.PageModule.makePage("coachList", coachTotalCnt, listSize, pageSize, cp);
	    }
	
	    model.addAttribute("coachList", coachList);
	    model.addAttribute("keyword", keyword);
	    model.addAttribute("certCount", certCount);
	    model.addAttribute("pageStr", pageStr);
	    model.addAttribute("sortColumn", sortColumn);
	    
	    model.addAttribute("contentPage", "admin/coach/coachList");
	    model.addAttribute("contentFragment", "coachContent");
	    
	    return "admin/dashboard";
	}
	
	
	@GetMapping("/coach/coachDetail")
	public String coachDetail(Model model,HttpSession session, @RequestParam int user_idx) {
		
		if(session.getAttribute("loginAdmin")==null) {
			return "redirect:/admin";
		}
		
		Map<String, Object> coachDetail = new HashMap<>();
		String status="";
		List<CertDTO> certList = new ArrayList<>();
		
		try {
			coachDetail = adminCoachService.getCoachDetail(user_idx);
			status = adminCoachService.getCoachStatus(user_idx);
			certList = adminCoachService.getWaitCertList(user_idx);
			
			model.addAttribute("coachStatus", status);
			
			if("SUSPENDED".equals(status)) {
				EmbeddedUserDTO dto = adminCoachService.getCoachEmbedded(user_idx);
				model.addAttribute("coachEmbedded", dto);
				
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		model.addAttribute("coachDetail", coachDetail);
		model.addAttribute("certList", certList);
		model.addAttribute("contentPage", "admin/coach/coachDetail");
		model.addAttribute("contentFragment", "coachDetail");
		
		return "admin/dashboard";
	}
	
	@PostMapping("/coach/updateCoachStatus")
	public String updateCoachSuspended(
			@RequestParam int user_idx,
			@RequestParam String status,
			EmbeddedUserDTO dto,
			RedirectAttributes ra,
			HttpSession session) {
		
		if(session.getAttribute("loginAdmin")==null) {
			return "redirect:/admin";
		}
		
		int updateresult = 0;
		int insertresult = 0;
		
		Map<String, Object> params = new HashMap<>();
		params.put("user_idx", user_idx);
	    params.put("status", status);
		
		try {
			updateresult = adminCoachService.updateCoachStatus(params);
			
			if(updateresult > 0) {
				if("SUSPENDED".equals(status)) {
					dto.setUser_idx(user_idx);
					insertresult = adminCoachService.insertCoachSuspended(dto);
					
					if(insertresult > 0) {
						ra.addFlashAttribute("msg", "계정이 정지 처리 되었습니다.");
					}
				}else if("ACTIVE".equals(status)) {		
					adminCoachService.updateEnddateSuspended(user_idx);
					ra.addFlashAttribute("msg", "사용 상태로 변경되었습니다.");
				}else if("WARNING".equals(status)) {
					adminCoachService.updateEnddateSuspended(user_idx);
					ra.addFlashAttribute("msg", "주의 상태로 변경되었습니다.");
				}else if("DANGER".equals(status)) {
					adminCoachService.updateEnddateSuspended(user_idx);
					ra.addFlashAttribute("msg", "위험 상태로 변경되었습니다.");
				}
				
			}else {
				ra.addFlashAttribute("msg", "상태 변경에 실패했습니다.");
			}	
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ra.addFlashAttribute("msg", "시스템 오류가 발생했습니다.");
		}
		
		return "redirect:/admin/coach/coachDetail?user_idx=" + user_idx;
	}
	
	//개인고객관리(자격증)
	@PostMapping("/coach/updateCertStatus")
	public String updateCertStatus(
			@RequestParam int user_idx,
			@RequestParam(value="cert_idx", required=false) List<Integer> certIdxList,
			@RequestParam(value="cert_name", required=false) List<String> certNameList,
			@RequestParam(value="get_date", required=false) List<String> getDateList,
			@RequestParam(value="cert_from", required=false) List<String> certFromList,
			@RequestParam(value="cert_status", required=false) List<String> certStatusList,
			@RequestParam(value="checked", required=false) List<Integer> checkedList,
			RedirectAttributes ra, HttpSession session) {
		
		if(session.getAttribute("loginAdmin")==null) {
			return "redirect:/admin";
		}
		
		try {
			
			if(checkedList != null && !checkedList.isEmpty()) {
				for(int i : checkedList) {
					CertDTO dto = new CertDTO();
					
					if (certIdxList != null && certIdxList.size() > i)
						dto.setCert_idx(certIdxList.get(i));
					if (certNameList != null && certNameList.size() > i)
						dto.setCert_name(certNameList.get(i));

					if (getDateList != null && getDateList.size() > i) {
						String dateStr = getDateList.get(i);
						if (dateStr != null && !dateStr.trim().isEmpty()) {
							dto.setGet_date(java.sql.Date.valueOf(dateStr));
						}
					}

					if (certFromList != null && certFromList.size() > i)
						dto.setCert_from(certFromList.get(i));
					if (certStatusList != null && certStatusList.size() > i)
						dto.setCert_status(certStatusList.get(i));
	                
	                adminCoachService.updateCertStatus(dto);
				}
				
				ra.addFlashAttribute("msg", "자격증 정보가 성공적으로 업데이트되었습니다.");
			}else {
				ra.addFlashAttribute("msg", "처리할 자격증을 선택해주세요.");
			}
			
		}catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ra.addFlashAttribute("msg", "시스템 오류가 발생했습니다.");
		}
		
		return "redirect:/admin/coach/coachDetail?user_idx=" + user_idx;
		
	}
	
	@GetMapping("/coach/getCertList")
	@ResponseBody
	public List<CertDTO> getCertList(HttpSession session, @RequestParam int user_idx) {
		
		if(session.getAttribute("loginAdmin")==null) {
			return null;
		}
		
		List<CertDTO> certList = new ArrayList<>();
		
		try {
			certList = adminCoachService.getCertList(user_idx);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return certList;

	}
	
	//매칭관리
	@GetMapping("/coach/matchList")
	public String getMatchList(Model model, HttpSession session,
			@RequestParam(value="cp", defaultValue="1") int cp,
			@RequestParam(value="keyword", required=false) String keyword) {
		
		if(session.getAttribute("loginAdmin")==null) {
			return "redirect:/admin";
		}
		
		int listSize = 10;
	    int pageSize = 5;
	    int matchTotalCnt = 0;
	    List<Map<String, Object>> matchList = new ArrayList<>();
	    Map<String, Object> statusCounts = new HashMap<>();
	    
	    try {
			matchTotalCnt = adminCoachService.getMatchTotalCnt(keyword);
			
			int start = (cp - 1) * listSize + 1;
	        int end = cp * listSize;
	        
	        matchList = adminCoachService.getMatchList(keyword, start, end);
	        
	        statusCounts = adminCoachService.getMatchStatusCounts();
		} catch (Exception e) {
			e.printStackTrace();
		}
	    
	    String pageStr = "";
	    if (keyword != null && !keyword.isEmpty()) {
	        String urlWithParams = "matchList?keyword=" + keyword;
	        pageStr = com.dacoach.page.PageModule.makePagewithParams(urlWithParams, matchTotalCnt, listSize, pageSize, cp);
	    } else {
	        pageStr = com.dacoach.page.PageModule.makePage("matchList", matchTotalCnt, listSize, pageSize, cp);
	    }
	    
	    model.addAttribute("matchList", matchList);
	    model.addAttribute("statusCounts", statusCounts);
	    model.addAttribute("keyword", keyword);
	    model.addAttribute("pageStr", pageStr);
		
		model.addAttribute("contentPage", "admin/coach/matchList");
	    model.addAttribute("contentFragment", "matchContent");
	    
	    return "admin/dashboard";
		
	}
	
	
	//코치 프로필 검수
	@GetMapping("/coach/coachInfoList")
	public String coachInfoList(Model model, HttpSession session,
			@RequestParam(value="cp", defaultValue="1") int cp) {
		
		if(session.getAttribute("loginAdmin")==null) {
			return "redirect:/admin";
		}
		
		int listSize=10;
		int pageSize=5;
		int coachInfoTotalCnt=0;
		List<Map<String, Object>> coachInfoList = new ArrayList<>();
		int coachCount=0;
		double avgRating=0.0;
		int totalTokens=0;
		
		try {	
			coachCount = adminCoachService.coachCount();
			avgRating = adminCoachService.avgRating();
			totalTokens = adminCoachService.totalTokens();
			
			coachInfoTotalCnt = adminCoachService.getCoachInfoTotalCnt();
			int start = (cp - 1) * listSize + 1;
			int end = cp * listSize;
			coachInfoList = adminCoachService.coachInfoList(start, end);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    
		String pageStr=com.dacoach.page.PageModule.makePage("coachInfoList", coachInfoTotalCnt, listSize, pageSize, cp);
		
		model.addAttribute("coachCount",coachCount);
		model.addAttribute("avgRating",avgRating);
		model.addAttribute("totalTokens",totalTokens);
		model.addAttribute("coachInfo",coachInfoList);
		model.addAttribute("pageStr",pageStr);
	    model.addAttribute("contentPage", "admin/coach/coachInfoList");
	    model.addAttribute("contentFragment", "coachInfoContent");
	    
	    return "admin/dashboard";
	}
	
	@GetMapping("/coach/infoDetail")
	public String coachInfoDetail(Model model, HttpSession session,
			@RequestParam int coach_idx) {
		
		if(session.getAttribute("loginAdmin")==null) {
			return "redirect:/admin";
		}
		
		CoachDTO dto = null;
		List<String> coachHashtags = null;
		
		try {
			
			dto = adminCoachService.coachInfoDetail(coach_idx);
			coachHashtags = adminCoachService.coachInfoHashtag(coach_idx);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		model.addAttribute("dto", dto);
		model.addAttribute("coachHashtags",coachHashtags);
		model.addAttribute("contentPage", "admin/coach/coachInfoDetail");
	    model.addAttribute("contentFragment", "coachInfoDetail");
	    
	    return "admin/dashboard";
		
	}
	
	
}
