package com.dacoach.model.classes;

import java.util.Date;
import org.springframework.format.annotation.DateTimeFormat;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class ClassDTO {

	private Integer class_idx;
	private Integer provider_idx;
	private String title;
	private String intro;
	private Integer minor_field_idx;
	private Integer minor_region_idx;
	private String region_detail;
	private Integer price;
	private String photo;
	private String video;

	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date start_date;

	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date end_date;

	private Date created_at;
	private Integer max_user_cnt;
}