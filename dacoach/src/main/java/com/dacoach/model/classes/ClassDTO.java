package com.dacoach.model.classes;

import java.util.Date;
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
	private Integer price;
	private Date start_date;
	private Date end_date;
	private Date created_at;
	private Integer max_user_cnt;
}