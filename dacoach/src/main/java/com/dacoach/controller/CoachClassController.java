package com.dacoach.controller;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.dacoach.model.coachClasses.CoachClassDTO;
import com.dacoach.service.coachClasses.CoachClassService;

@Controller
@RequestMapping("/class")
public class CoachClassController {
	
	@Autowired
	private CoachClassService classService;

	/************* coach *************/
	// 코치 - 클래스 검색
	@GetMapping("/coach/classList")
	public ModelAndView coachClassList(@RequestParam(required = false) Integer majorField,
			@RequestParam(required = false) Integer minorField, @RequestParam(required = false) Integer majorRegion,
			@RequestParam(required = false) Integer minorRegion, @RequestParam(required = false) String q,
			@RequestParam(required = false, defaultValue = "latest") String sort) throws Exception {
		ModelAndView mav = new ModelAndView();

		List<Map<String, Object>> majorList = classService.getMajorFields();
		List<Map<String, Object>> majorRegions = classService.getMajorRegions();

		List<CoachClassDTO> classList = classService.classSearch(majorField, minorField, majorRegion, minorRegion, q, sort);

		mav.addObject("majorList", majorList);
		mav.addObject("majorRegions", majorRegions);
		mav.addObject("classList", classList);

		// 검색값 유지
		mav.addObject("majorField", majorField);
		mav.addObject("minorField", minorField);
		mav.addObject("majorRegion", majorRegion);
		mav.addObject("minorRegion", minorRegion);
		mav.addObject("q", q);
		mav.addObject("sort", sort);

		mav.setViewName("coach/classes/classList");
		return mav;
	}

	// 코치 - 클래스 상세
	@GetMapping("/coach/classDetail")
	public ModelAndView coachClassDetail(@RequestParam("id") int id) throws Exception {
		ModelAndView mav = new ModelAndView();
		CoachClassDTO classDTO = classService.getClassDetail(id); // id로 조회

	    if (classDTO == null) {
	        // 1) 없는 id면 목록으로 보내거나
	        mav.setViewName("redirect:/class/coach/classList");
	        return mav;

	        // 또는 2) 에러 페이지/메시지 보여주고 싶으면
	        // mav.addObject("error", "존재하지 않는 클래스입니다.");
	        // return mav;
	    }

	    mav.addObject("classDTO", classDTO);

	    // 일단 화면 안 터지게 최소값도 같이
	    mav.addObject("providerName", "");   // 나중에 채우기
	    mav.addObject("providerPhoto", "");
	    mav.addObject("avgRating", 0);
	    mav.addObject("reviewCount", 0);
	    mav.addObject("reviewList", Collections.emptyList());
	    mav.addObject("tagList", Collections.emptyList());
	    mav.setViewName("coach/classes/classDetail");
	    return mav;
	}
	/*********************************/
}
