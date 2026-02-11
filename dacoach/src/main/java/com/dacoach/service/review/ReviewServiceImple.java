package com.dacoach.service.review;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dacoach.mapper.review.ReviewMapper;
import com.dacoach.model.review.ReviewClassDTO;
import com.dacoach.model.review.ReviewCoachDTO;

@Service
public class ReviewServiceImple implements ReviewService {

    @Autowired
    private ReviewMapper reviewMapper;

    @Override
    public String getUserType(int user_idx) {
        return reviewMapper.selectUserType(user_idx);
    }

    @Override
    public List<ReviewClassDTO> getWrittenClassReviews(int user_idx) {
        return reviewMapper.selectWrittenClassReviews(user_idx);
    }

    @Override
    public List<ReviewCoachDTO> getWrittenCoachReviews(int user_idx) {
        return reviewMapper.selectWrittenCoachReviews(user_idx);
    }

    @Override
    public List<ReviewClassDTO> getReceivedClassReviewsByProvider(int provider_idx) {
        return reviewMapper.selectReceivedClassReviewsByProvider(provider_idx);
    }

    @Override
    public List<ReviewCoachDTO> getReceivedCoachReviews(int user_idx) {
        return reviewMapper.selectReceivedCoachReviews(user_idx);
    }

    @Override
    public int deleteCoachReview(int review_coach_idx, int user_idx) {
        String type = getUserType(user_idx);
        if (!"coach".equals(type)) return 0;
        return reviewMapper.deleteCoachReview(review_coach_idx, user_idx);
    }

    @Override
    public int deleteClassReview(int review_class_idx, int user_idx) {
        String type = getUserType(user_idx);
        if (!"coach".equals(type)) return 0;
        return reviewMapper.deleteClassReview(review_class_idx, user_idx);
    }

	@Override
	public List<ReviewCoachDTO> getCoachReviews(int target_idx) {
		List<ReviewCoachDTO> reviews = reviewMapper.getCoachReviews(target_idx);
		return reviews;
	}
}
