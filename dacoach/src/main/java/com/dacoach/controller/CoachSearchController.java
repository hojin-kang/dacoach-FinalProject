package com.dacoach.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.dacoach.model.coach.CoachDTO;
import com.dacoach.service.coach.CoachService;
import com.dacoach.service.coachSearch.CoachSearchService;

import jakarta.servlet.http.HttpSession;

@Controller
public class CoachSearchController {
		@Autowired
		private CoachService coachService;
		@Autowired
		private CoachSearchService coachSearchService;
		
	 	@GetMapping("/coach/search")
	public ModelAndView coachSearchForm(@RequestParam(value="cp", defaultValue = "1") int cp,
			@RequestParam(value="keyword", defaultValue = "") String keyword,
			@RequestParam(value="sort", defaultValue = "latest") String sort,
			@RequestParam(value="majorField", defaultValue = "0") int majorField,
			@RequestParam(value="minorField", defaultValue = "0") int minorField,
			@RequestParam(value="majorRegion", defaultValue = "0") int majorRegion,
			@RequestParam(value="minorRegion", defaultValue = "0") int minorRegion
			) {
		ModelAndView mav = new ModelAndView();
		//대분류(분야, 지역) 리스트 담기
		List<Map<String, Object>> majorFields = null;
		List<Map<String, Object>> majorRegions = null;
		try {
			majorFields = coachService.getMajorFields();
			majorRegions = coachService.getMajorRegions();
			mav.addObject("majorFields", majorFields);
			mav.addObject("majorRegions", majorRegions);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//코치 검색 결과 담기
		List<CoachDTO> coachList = null;
		try {
			HashMap<String,Object> map = new HashMap<String,Object>();
			map.put("keyword", keyword);
			map.put("majorField", majorField);
			map.put("minorField", minorField);
			map.put("majorRegion", majorRegion);
			map.put("minorRegion", minorRegion);
			map.put("keyword", keyword);
			map.put("sort", sort);
			coachList = coachSearchService.coachList(cp, map);
			if (coachList != null) {
			    for (CoachDTO coach : coachList) {
			        // DB에서 해당 코치의 태그를 가져와서 DTO에 바로 세팅
			        List<String> tags = coachSearchService.getCoachHashtags(coach.getUser_idx());
			        coach.setHashtags(tags);
			    }
			}
			mav.addObject("coachList", coachList);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		mav.addObject("sort", sort);
		mav.setViewName("coach/coachSearch");
		return mav;
	}
	@GetMapping("/coach/detail")
	public ModelAndView coachDetail(@RequestParam(value="user_idx", defaultValue = "0") int user_idx,
			HttpSession session
			) {
		ModelAndView mav = new ModelAndView();
		//로그인 중이 아닐경우
		if(session.getAttribute("user_idx") == null) {
			mav.addObject("msg", "코치 상세정보는 로그인 후 이용 가능합니다.");
			mav.addObject("url", "/login");
			mav.setViewName("alert");
			return mav;
		}
		//코치상세정보담기
		try {
			CoachDTO cdto = coachService.getCoachInfo(user_idx);
			List<String> myHashtags = coachSearchService.getCoachHashtags(user_idx);
			mav.addObject("myHashtags", myHashtags);
			mav.addObject("dto", cdto);
			mav.setViewName("coach/detail");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return mav;
	}
	 	
}
