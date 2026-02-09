package com.dacoach.controller;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.dacoach.model.coachClasses.CoachClassDTO;
import com.dacoach.service.coachClasses.CoachClassService;
import com.dacoach.service.likes.LikesService;

import jakarta.servlet.http.HttpSession;

@Controller
public class CoachClassController {

	@Autowired
	private CoachClassService classService;

	@Autowired
	private LikesService likesClassService;

	@GetMapping("/majorFields")
	@ResponseBody
	public List<Map<String, Object>> getMajorFields() throws Exception {
		return classService.getMajorFields();
	}

	@GetMapping("/minorFields")
	@ResponseBody
	public List<Map<String, Object>> getMinorFields(@RequestParam Integer majorFieldIdx) throws Exception {
		return classService.getMinorFields(majorFieldIdx);
	}

	@GetMapping("/majorRegions")
	@ResponseBody
	public List<Map<String, Object>> getMajorRegions() throws Exception {
		return classService.getMajorRegions();
	}

	@GetMapping("/minorRegions")
	@ResponseBody
	public List<Map<String, Object>> getMinorRegions(@RequestParam Integer majorRegionIdx) throws Exception {
		return classService.getMinorRegions(majorRegionIdx);
	}

	// 코치 - 클래스 검색
	@GetMapping("/coach/classList")
	public ModelAndView coachClassList(
			@RequestParam(required = false) Integer majorField,
			@RequestParam(required = false) Integer minorField,
			@RequestParam(required = false) Integer majorRegion,
			@RequestParam(required = false) Integer minorRegion,
			@RequestParam(required = false) String q,
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
	public ModelAndView coachClassDetail(@RequestParam("id") int id, HttpSession session) throws Exception {
		ModelAndView mav = new ModelAndView();

		CoachClassDTO classDTO = classService.getClassDetail(id);
		if (classDTO == null) {
			mav.setViewName("redirect:/coach/classList");
			return mav;
		}

		mav.addObject("classDTO", classDTO);

		// isLiked 계산
		boolean isLiked = false;
		Integer userIdx = (Integer) session.getAttribute("user_idx");
		if (userIdx != null) {
			isLiked = likesClassService.isLikedClass(id, userIdx);
		}
		mav.addObject("isLiked", isLiked);

		// 화면 최소값
		mav.addObject("providerPhoto", "");
		mav.addObject("avgRating", 0);
		mav.addObject("reviewCount", 0);
		mav.addObject("reviewList", Collections.emptyList());
		mav.addObject("tagList", Collections.emptyList());

		mav.setViewName("coach/classes/classDetail");
		return mav;
	}
}
