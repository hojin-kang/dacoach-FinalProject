package com.dacoach.service.review;

import java.util.List;
import com.dacoach.model.review.ReviewClassDTO;
import com.dacoach.model.review.ReviewCoachDTO;

public interface ReviewService {
    String getUserType(int user_idx);

    List<ReviewClassDTO> getWrittenClassReviews(int user_idx);
    List<ReviewCoachDTO> getWrittenCoachReviews(int user_idx);

    List<ReviewClassDTO> getReceivedClassReviewsByProvider(int provider_idx);
    List<ReviewCoachDTO> getReceivedCoachReviews(int user_idx);

    int deleteCoachReview(int review_coach_idx, int user_idx);
    int deleteClassReview(int review_class_idx, int user_idx);
    
    List<ReviewCoachDTO> getCoachReviews(int target_idx);
}
