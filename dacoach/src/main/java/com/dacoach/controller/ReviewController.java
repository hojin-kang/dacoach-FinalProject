package com.dacoach.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.dacoach.model.coach.CoachDTO;
import com.dacoach.model.coachClasses.CoachClassDTO;
import com.dacoach.model.review.ReviewClassDTO;
import com.dacoach.service.coach.CoachService;
import com.dacoach.service.review.ReviewService;

import jakarta.servlet.http.HttpSession;


@Controller
public class ReviewController {

    @Autowired
    private ReviewService reviewService;
    @Autowired
    private CoachService coachService;

    @PostMapping("/review/coach/delete")
    public String deleteCoachReview(@RequestParam("review_coach_idx") int review_coach_idx,
                                    HttpSession session) {
        Integer user_idx = (Integer) session.getAttribute("user_idx");
        if (user_idx == null || user_idx == 0) return "redirect:/needLogin";

        // COACH만 삭제 가능
        String type = reviewService.getUserType(user_idx);
        if (!"coach".equals(type)) return "redirect:/myReview";

        reviewService.deleteCoachReview(review_coach_idx, user_idx);
        return "redirect:/myReview";
    }
    
    @PostMapping("/review/class/delete")
    public String deleteClassReview(@RequestParam("review_class_idx") int review_class_idx,
                                    HttpSession session) {
        Integer user_idx = (Integer) session.getAttribute("user_idx");
        if (user_idx == null || user_idx == 0) return "redirect:/needLogin";

        // COACH만 삭제 가능
        String type = reviewService.getUserType(user_idx);
        if (!"coach".equals(type)) return "redirect:/myReview";

        reviewService.deleteClassReview(review_class_idx, user_idx);
        return "redirect:/myReview";
    }
    //후기 작성 폼
	@GetMapping("/coach/coachReview")
	public ModelAndView writeReviewPage(@RequestParam("target_idx") int target_idx, HttpSession session) throws Exception {
		ModelAndView mav = new ModelAndView();

		// 로그인 확인
		Integer userIdx = (Integer) session.getAttribute("user_idx");
		if (userIdx == null) {
			mav.setViewName("redirect:/login");
			return mav;
		}
		//후기 작성 기한 확인
		Long expiry = (Long) session.getAttribute("reviewExpiry");
		Boolean canReview = (Boolean) session.getAttribute("reviewDispo");
		// 만료 시간이 지났거나, 권한이 없다면 실행 차단
		if (expiry == null || canReview == null || System.currentTimeMillis() > expiry) {
		    session.removeAttribute("reviewDispo");
		    session.removeAttribute("reviewExpiry");
		    mav.setViewName("/alert");
		    mav.addObject("msg", "비정상적인 접근입니다.");
		    mav.addObject("url", "/");
		    return mav; 
		}

		// 코치 정보 조회
		CoachDTO dto = coachService.getCoachInfo(target_idx);
		if (dto == null) {
			mav.setViewName("redirect:/");
			mav.addObject("msg", "존재하지 않는 회원입니다.");
		    mav.addObject("url", "/");
			return mav;
		}

		// 후기 태그 목록 조회 (REVIEW_TAG 테이블에서 코치 타입)
		List<String> reviewTags = coachService.getCoachReviewTags();

		mav.addObject("dto", dto);
		mav.addObject("reviewTags", reviewTags);
		mav.setViewName("coach/coachReview");
		return mav;
	}

	/**
	 * 후기 작성 처리 (POST)
	 */
	@PostMapping("/coach/insertReview")
	public String submitReview(@RequestParam("target_idx") int target_idx, @RequestParam("rating") double rating,
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
			review.setClass_idx(target_idx);
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

			boolean success = coachService.writeCoachReview(review);

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

		return "redirect:/";
	}

}
