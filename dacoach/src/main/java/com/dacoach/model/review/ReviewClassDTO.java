package com.dacoach.model.review;

import java.sql.Date;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
@AllArgsConstructor
@Getter
@Setter
@ToString
@NoArgsConstructor
public class ReviewClassDTO {
	private int review_class_idx;
	private int reviewer_idx;
	private int class_idx;
	private double rating;
	private String tag;
	private String content;
	private Date created_at;
	
	// join 결과
    private String class_title;
    private String provider_name;  // written용(내가 쓴 리뷰에서 제공자 표시)
    
    // 프로필 이미지
    private String reviewer_photo;
    private String provider_photo;

    // 라벨
    private String reviewer_label;
    private String provider_label;
    
    private List<String> tagList;
}
