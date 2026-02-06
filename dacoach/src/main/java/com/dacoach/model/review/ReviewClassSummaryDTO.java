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
public class ReviewClassSummaryDTO {
	  private double avg_rating;
	  private int review_count;
	  private int class_idx;
}