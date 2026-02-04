package com.dacoach.model.coachClasses;

import java.util.Date;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class CoachClassDTO {

	private Integer class_idx;
	private Integer provider_idx;
	private String title;
	private String intro;
	private Integer minor_field_idx;
	private Integer minor_region_idx;
	private Integer price;
	private String photo;              
	private String video;              
	
	@DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
	private Date start_date;
	
	@DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
	private Date end_date;
	
	private Date created_at;
	private Integer max_user_cnt;
	
	// 파일 업로드용 (DB에는 저장하지 않음, transient)
	private MultipartFile photoFile;   // 사진 파일
	private MultipartFile videoFile;   // 영상 파일
	
	// 리뷰용
	private String user_name;   // 회사명
	private Double avg_rating;  // 별점
	private Integer review_cnt;  // 리뷰 수
}