package com.dacoach.model.review;

import java.sql.Date;

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
}
