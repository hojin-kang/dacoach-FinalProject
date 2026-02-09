package com.dacoach.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.dacoach.service.review.ReviewService;

import jakarta.servlet.http.HttpSession;


@Controller
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

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

}
