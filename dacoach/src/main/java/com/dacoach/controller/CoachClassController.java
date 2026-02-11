package com.dacoach.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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

	// ===== 기존 메서드 =====

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

	@GetMapping("/coach/classList")
	public ModelAndView coachClassList(@RequestParam(required = false) Integer majorField,
			@RequestParam(required = false) Integer minorField, @RequestParam(required = false) Integer majorRegion,
			@RequestParam(required = false) Integer minorRegion, @RequestParam(required = false) String q,
			@RequestParam(required = false, defaultValue = "latest") String sort) throws Exception {

		ModelAndView mav = new ModelAndView();

		List<Map<String, Object>> majorList = classService.getMajorFields();
		List<Map<String, Object>> majorRegions = classService.getMajorRegions();
		List<CoachClassDTO> classList = classService.classSearch(majorField, minorField, majorRegion, minorRegion, q,
				sort);

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

	@GetMapping("/coach/classDetail")
	public ModelAndView coachClassDetail(@RequestParam("id") int id, HttpSession session) throws Exception {
		ModelAndView mav = new ModelAndView();

		// 1. 클래스 기본 정보
		CoachClassDTO classDTO = classService.getClassDetail(id);
		if (classDTO == null) {
			mav.setViewName("redirect:/coach/classList");
			return mav;
		}
		mav.addObject("classDTO", classDTO);

		// 2. 찜 여부 (isLiked 계산)
		boolean isLiked = false;
		Integer userIdx = (Integer) session.getAttribute("user_idx");
		if (userIdx != null) {
			isLiked = likesClassService.isLikedClass(id, userIdx);
		}
		mav.addObject("isLiked", isLiked);

		// 2-1. 수강 신청 여부 확인
		boolean isEnrolled = false;
		if (userIdx != null) {
			isEnrolled = classService.isUserEnrolled(id, userIdx);
		}
		mav.addObject("isEnrolled", isEnrolled);

		// 3. 해시태그 목록 조회
		List<String> hashtagList = classService.getHashtagsByClass(id);
		mav.addObject("hashtagList", hashtagList);

		// 4. 분야 정보 조회 (대분류 > 소분류)
		Map<String, Object> fieldInfo = classService.getClassFieldInfo(id);
		mav.addObject("fieldInfo", fieldInfo);

		// 5. 지역 정보 조회 (대지역 > 소지역)
		Map<String, Object> regionInfo = classService.getClassRegionInfo(id);
		mav.addObject("regionInfo", regionInfo);

		// 6. 제공자 정보 조회 (이름, 사진)
		Map<String, Object> providerInfo = classService.getProviderInfo(classDTO.getProvider_idx());
		mav.addObject("providerPhoto", providerInfo.get("PROVIDER_PHOTO"));

		// 7. 리뷰 정보 조회
		List<Map<String, Object>> reviewList = classService.getReviewsByClass(id);
		Double avgRating = classService.getAvgRatingByClass(id);
		Integer reviewCount = classService.getReviewCountByClass(id);

		mav.addObject("reviewList", reviewList);
		mav.addObject("avgRating", avgRating);
		mav.addObject("reviewCount", reviewCount);

		mav.setViewName("coach/classes/classDetail");
		return mav;
	}

	// ===== 수강신청 관련 메서드 추가 =====

	/**
	 * 수강신청 페이지
	 */
	@GetMapping("/coach/classEnrollment")
	public ModelAndView classEnrollmentPage(@RequestParam("classId") int classId, HttpSession session)
			throws Exception {
		ModelAndView mav = new ModelAndView();

		// 로그인 확인
		Integer userIdx = (Integer) session.getAttribute("user_idx");
		if (userIdx == null) {
			mav.setViewName("redirect:/login");
			return mav;
		}

		// 클래스 정보 조회
		CoachClassDTO classDTO = classService.getClassDetail(classId);
		if (classDTO == null) {
			mav.setViewName("redirect:/coach/classList");
			return mav;
		}

		mav.addObject("classDTO", classDTO);
		mav.setViewName("coach/classes/classEnrollment");
		return mav;
	}

	/**
	 * 특정 날짜의 수강 신청 인원 수 조회 (Ajax)
	 */
	@GetMapping("/coach/enrollmentCount")
	@ResponseBody
	public Map<String, Object> getEnrollmentCount(@RequestParam("classId") int classId,
			@RequestParam("date") String date) throws Exception {

		Map<String, Object> result = new HashMap<>();

		try {
			Integer count = classService.getEnrollmentCountByDate(classId, date);
			CoachClassDTO classInfo = classService.getClassDetail(classId);

			result.put("success", true);
			result.put("currentCount", count);
			result.put("maxCount", classInfo.getMax_user_cnt());
			result.put("available", classInfo.getMax_user_cnt() - count);
		} catch (Exception e) {
			result.put("success", false);
			result.put("message", e.getMessage());
		}

		return result;
	}

	/**
	 * 수강 신청 처리 (POST)
	 */
	@PostMapping("/coach/enrollClass")
	public String enrollClass(@RequestParam("classId") int classId,
			@RequestParam("enrollmentDate") String enrollmentDate, HttpSession session,
			RedirectAttributes redirectAttributes) throws Exception {

		Integer userIdx = (Integer) session.getAttribute("user_idx");
		if (userIdx == null) {
			return "redirect:/login";
		}

		try {
			boolean success = classService.enrollClass(classId, userIdx, enrollmentDate);

			if (success) {
				redirectAttributes.addFlashAttribute("message", "수강 신청이 완료되었습니다.");
				redirectAttributes.addFlashAttribute("messageType", "success");
			} else {
				redirectAttributes.addFlashAttribute("message", "수강 신청에 실패했습니다.");
				redirectAttributes.addFlashAttribute("messageType", "error");
			}
		} catch (IllegalStateException e) {
			redirectAttributes.addFlashAttribute("message", e.getMessage());
			redirectAttributes.addFlashAttribute("messageType", "error");
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("message", "오류가 발생했습니다: " + e.getMessage());
			redirectAttributes.addFlashAttribute("messageType", "error");
		}

		return "redirect:/coach/classDetail?id=" + classId;
	}

	/**
	 * 내 수강 신청 목록 조회 URL: /coach/myEnrollment View: coach/classes/myEnrollment.html
	 */
	@GetMapping("/coach/myEnrollment")
	public ModelAndView myEnrollment(HttpSession session) throws Exception {
		ModelAndView mav = new ModelAndView();

		Integer userIdx = (Integer) session.getAttribute("user_idx");
		if (userIdx == null) {
			mav.setViewName("redirect:/login");
			return mav;
		}

		// 내 수강 신청 목록 조회
		var enrollmentList = classService.getEnrollmentsByUser(userIdx);
		mav.addObject("enrollmentList", enrollmentList);
		mav.setViewName("coach/classes/myEnrollment");
		return mav;
	}

	/**
	 * 수강 신청 취소
	 */
	@PostMapping("/coach/cancelEnrollment")
	public String cancelEnrollment(@RequestParam("enrollIdx") int enrollIdx, RedirectAttributes redirectAttributes)
			throws Exception {

		try {
			boolean success = classService.cancelEnrollment(enrollIdx);

			if (success) {
				redirectAttributes.addFlashAttribute("message", "수강 신청이 취소되었습니다.");
				redirectAttributes.addFlashAttribute("messageType", "success");
			} else {
				redirectAttributes.addFlashAttribute("message", "취소에 실패했습니다.");
				redirectAttributes.addFlashAttribute("messageType", "error");
			}
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("message", "오류가 발생했습니다: " + e.getMessage());
			redirectAttributes.addFlashAttribute("messageType", "error");
		}

		return "redirect:/coach/myEnrollment";
	}
}