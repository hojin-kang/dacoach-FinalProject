package com.dacoach.mapper.review;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.dacoach.model.review.ReviewClassDTO;
import com.dacoach.model.review.ReviewCoachDTO;

@Mapper
public interface ReviewMapper {

    String selectUserType(@Param("user_idx") int user_idx);

    // 코치리뷰
    List<ReviewCoachDTO> selectWrittenCoachReviews(@Param("user_idx") int user_idx);
    List<ReviewCoachDTO> selectReceivedCoachReviews(@Param("user_idx") int user_idx);

    // 클래스리뷰
    List<ReviewClassDTO> selectWrittenClassReviews(@Param("user_idx") int user_idx);
    List<ReviewClassDTO> selectReceivedClassReviewsByProvider(@Param("provider_idx") int provider_idx);

    // 삭제
    int deleteCoachReview(@Param("review_coach_idx") int review_coach_idx, @Param("user_idx") int user_idx);
    int deleteClassReview(@Param("review_class_idx") int review_class_idx, @Param("user_idx") int user_idx);
    
    // 코치 상세/전체리뷰
    List<ReviewCoachDTO> getCoachReviews(@Param("target_idx") int target_idx);

    // 페이징용
    int getCoachReviewCount(@Param("target_idx") int target_idx);

    List<ReviewCoachDTO> getCoachReviewsPaged(
            @Param("target_idx") int target_idx,
            @Param("start") int start,
            @Param("end") int end
    );
}
