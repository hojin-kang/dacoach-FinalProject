package com.dacoach.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.dacoach.model.coachClasses.CoachClassDTO;
import com.dacoach.model.review.ReviewClassDTO;
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

		// 2-2. 후기 작성 여부 확인
		boolean hasReviewed = false;
		if (userIdx != null) {
			hasReviewed = classService.hasUserReviewedClass(id, userIdx);
		}
		mav.addObject("hasReviewed", hasReviewed);

		// 2-3. 수강 완료 여부 확인 (수강 날짜가 오늘이거나 과거인지)
		boolean hasCompleted = false;
		if (userIdx != null) {
			hasCompleted = classService.hasUserCompletedEnrollment(id, userIdx);
		}
		mav.addObject("hasCompleted", hasCompleted);

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

	// ===== 수강신청 관련 메서드 =====

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

		// 이미 신청한 날짜 목록 조회
		List<String> enrolledDates = classService.getUserEnrolledDates(classId, userIdx);

		mav.addObject("classDTO", classDTO);
		mav.addObject("enrolledDates", enrolledDates);
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

		// 각 enrollment의 취소 가능 여부를 Map으로 저장
		Map<Integer, Boolean> cancelMap = new HashMap<>();

		// 오늘 날짜 (00:00:00)
		java.util.Date today = new java.util.Date();
		java.util.Calendar cal = java.util.Calendar.getInstance();
		cal.setTime(today);
		cal.set(java.util.Calendar.HOUR_OF_DAY, 0);
		cal.set(java.util.Calendar.MINUTE, 0);
		cal.set(java.util.Calendar.SECOND, 0);
		cal.set(java.util.Calendar.MILLISECOND, 0);
		java.util.Date todayStart = cal.getTime();

		for (var enrollment : enrollmentList) {
			boolean canCancel = true;

			// 1. 이미 취소되었거나 완료된 경우
			if ("CANCELLED".equals(enrollment.getStatus()) || "COMPLETED".equals(enrollment.getStatus())) {
				canCancel = false;
			}
			// 2. 수강 예정일이 오늘이거나 과거인 경우
			else if (enrollment.getCompleted_at() != null) {
				java.util.Calendar completedCal = java.util.Calendar.getInstance();
				completedCal.setTime(enrollment.getCompleted_at());
				completedCal.set(java.util.Calendar.HOUR_OF_DAY, 0);
				completedCal.set(java.util.Calendar.MINUTE, 0);
				completedCal.set(java.util.Calendar.SECOND, 0);
				completedCal.set(java.util.Calendar.MILLISECOND, 0);
				java.util.Date completedDateOnly = completedCal.getTime();

				// 수강 예정일이 오늘이거나 과거면 취소 불가
				if (!completedDateOnly.after(todayStart)) {
					canCancel = false;
				}
			}

			// Map에 저장 (key: enroll_idx, value: canCancel)
			cancelMap.put(enrollment.getEnroll_idx(), canCancel);
		}

		mav.addObject("enrollmentList", enrollmentList);
		mav.addObject("cancelMap", cancelMap); // Map 전달
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

	// ===== 후기 관련 메서드 추가 =====

	/**
	 * 후기 작성 페이지
	 */
	@GetMapping("/coach/reviewWrite")
	public ModelAndView writeReviewPage(@RequestParam("classId") int classId, HttpSession session) throws Exception {
		ModelAndView mav = new ModelAndView();

		// 로그인 확인
		Integer userIdx = (Integer) session.getAttribute("user_idx");
		if (userIdx == null) {
			mav.setViewName("redirect:/login");
			return mav;
		}

		// 수강 완료 여부 확인 (수강 날짜가 오늘이거나 과거인지)
		if (!classService.hasUserCompletedEnrollment(classId, userIdx)) {
			mav.setViewName("redirect:/coach/classDetail?id=" + classId);
			return mav;
		}

		// 이미 후기를 작성했는지 확인
		if (classService.hasUserReviewedClass(classId, userIdx)) {
			mav.setViewName("redirect:/coach/classDetail?id=" + classId);
			return mav;
		}

		// 클래스 정보 조회
		CoachClassDTO classDTO = classService.getClassDetail(classId);
		if (classDTO == null) {
			mav.setViewName("redirect:/coach/classList");
			return mav;
		}

		// 후기 태그 목록 조회 (REVIEW_TAG 테이블에서 CLASS 타입)
		List<String> reviewTags = classService.getClassReviewTags();

		mav.addObject("classDTO", classDTO);
		mav.addObject("reviewTags", reviewTags);
		mav.setViewName("coach/classes/reviewWrite");
		return mav;
	}

	/**
	 * 후기 작성 처리 (POST)
	 */
	@PostMapping("/coach/submitReview")
	public String submitReview(@RequestParam("classId") int classId, @RequestParam("rating") double rating,
			@RequestParam(value = "tags", required = false) List<String> tags,
			@RequestParam(value = "content", required = false) String content, HttpSession session,
			RedirectAttributes redirectAttributes) throws Exception {

		Integer userIdx = (Integer) session.getAttribute("user_idx");
		if (userIdx == null) {
			return "redirect:/login";
		}

		try {
			// ReviewClassDTO 생성
			ReviewClassDTO review = new ReviewClassDTO();
			review.setClass_idx(classId);
			review.setReviewer_idx(userIdx);
			review.setRating(rating);
			review.setContent(content != null ? content : "");

			// 태그들을 하나의 문자열로 합치기 (예: "#친절해요 #재미있어요")
			if (tags != null && !tags.isEmpty()) {
				String tagString = String.join(" ", tags);
				review.setTag(tagString);
			} else {
				review.setTag("");
			}

			boolean success = classService.writeClassReview(review);

			if (success) {
				redirectAttributes.addFlashAttribute("message", "후기가 등록되었습니다.");
				redirectAttributes.addFlashAttribute("messageType", "success");
			} else {
				redirectAttributes.addFlashAttribute("message", "후기 등록에 실패했습니다.");
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

	@PostMapping("/class/like")
	@ResponseBody
	public Map<String, String> likeCoach(@RequestBody Map<String, Integer> params, HttpSession session) {
		Map<String, String> response = new HashMap<>();
		Integer login_idx = (Integer) session.getAttribute("user_idx");

		if (login_idx == null) {
			response.put("status", "login_required");
			return response;
		}

		try {
			int target_idx = params.get("target_idx");
			int result = classService.likeClass(login_idx, target_idx);

			if (result > 0) {
				response.put("status", "success");
			} else {
				response.put("status", "error");
			}
		} catch (Exception e) {
			e.printStackTrace();
			response.put("status", "error");
		}
		return response; //
	}

	/**
	 * 환불요청 페이지
	 */
	@GetMapping("/coach/refundForm")
	public ModelAndView refundForm(@RequestParam("enrollIdx") int enrollIdx, HttpSession session) throws Exception {
		ModelAndView mav = new ModelAndView();

		Integer userIdx = (Integer) session.getAttribute("user_idx");
		if (userIdx == null) {
			mav.setViewName("redirect:/login");
			return mav;
		}

		var enrollment = classService.getEnrollmentDetail(enrollIdx);
		if (enrollment == null) {
			mav.setViewName("redirect:/coach/myEnrollment");
			return mav;
		}

		mav.addObject("enrollment", enrollment);
		mav.setViewName("coach/classes/refundRequest");
		return mav;
	}

	/**
	 * 환불요청 처리 (POST)
	 */
	@PostMapping("/coach/refundRequest")
	public ModelAndView refundRequest(@RequestParam("enroll_idx") int enrollIdx,
			@RequestParam("class_idx") int classIdx, @RequestParam("payload") String payload, HttpSession session)
			throws Exception {

		ModelAndView mav = new ModelAndView();

		Integer userIdx = (Integer) session.getAttribute("user_idx");
		if (userIdx == null) {
			mav.setViewName("redirect:/login");
			return mav;
		}

		try {
			classService.requestRefund(enrollIdx, userIdx, payload);
		} catch (Exception e) {
			e.printStackTrace();
			mav.addObject("msg", "환불 요청 중 오류가 발생했습니다: " + e.getMessage());
			mav.addObject("url", "/coach/myEnrollment");
			mav.setViewName("alert");
			return mav;
		}

		mav.addObject("msg", "수강신청 취소되었습니다. 환불은 관리자에 의해 빠른 시일 내에 진행해드리겠습니다.");
		mav.addObject("url", "/coach/myEnrollment");
		mav.setViewName("alert");
		return mav;
	}

	@PostMapping("/class/unlike")
	@ResponseBody
	public Map<String, String> unlikeCoach(@RequestBody Map<String, Integer> params, HttpSession session) {
		Map<String, String> response = new HashMap<>();
		Integer login_idx = (Integer) session.getAttribute("user_idx");

		if (login_idx == null) {
			response.put("status", "login_required");
			return response;
		}

		try {
			int target_idx = params.get("target_idx");
			int result = classService.unlikeClass(login_idx, target_idx);

			if (result > 0) {
				response.put("status", "success");
			} else {
				response.put("status", "error");
			}
		} catch (Exception e) {
			e.printStackTrace();
			response.put("status", "error");
		}
		return response; //
	}
}