package com.dacoach.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.*;

import com.dacoach.model.coach.CoachDTO;
import com.dacoach.service.coachClasses.CoachClassService;
import com.dacoach.service.coachSearch.CoachSearchService;

import jakarta.servlet.http.HttpSession;

@Controller
public class SearchController {

	@Autowired
    private CoachSearchService coachSearchService;
	
	@Autowired
    private CoachClassService classService;
	
	@GetMapping("/searchResult")
	    public String searchResult(@RequestParam(name="keyword", required=false, defaultValue="") String keyword,
	                               HttpSession session,
	                               Model model) {

	        // 로그인 idx (coachSearch 쿼리에서 상태값/chatStatus/matchStatus 계산할 때 씀)
	        int loginIdx = session.getAttribute("user_idx") == null ? 0 : (int) session.getAttribute("user_idx");

	        // coachSearchService.coachList(cp, map) 재사용 (cp=1만 일단 보여주자)
	        HashMap<String, Object> map = new HashMap<>();
	        map.put("keyword", keyword);
	        map.put("sort", "latest");
	        map.put("majorField", 0);
	        map.put("minorField", 0);
	        map.put("majorRegion", 0);
	        map.put("minorRegion", 0);
	        map.put("login_idx", loginIdx);

	        List<CoachDTO> coachList = null;
			try {
				coachList = coachSearchService.coachList(1, map);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	        int coachCnt = (coachList != null && !coachList.isEmpty()) ? coachList.get(0).getTotal_cnt() : 0;

	        model.addAttribute("keyword", keyword);
	        model.addAttribute("coachList", coachList);
	        model.addAttribute("coachCnt", coachCnt);

	        return "searchResult";
	    }
}
