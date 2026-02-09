package com.dacoach.model.review;

import java.sql.Date;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
@ToString
public class ReviewCoachDTO {
    private int review_coach_idx;
    private int reviewer_idx;
    private int reviewee_idx;
    private double rating;
    private String tag;
    private String content;
    private Date created_at;

    // join 결과
    private String reviewer_name;
    private String reviewee_name;
    
    // 프로필 이미지(코치=COACH.PHOTO, 회사=COMPANY.PHOTO)
    private String reviewer_photo;
    private String reviewee_photo;

    // 라벨(유형/랭크)
    private String reviewer_label;
    private String reviewee_label;
}
