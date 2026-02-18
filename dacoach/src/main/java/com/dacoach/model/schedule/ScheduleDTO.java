package com.dacoach.model.schedule;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class ScheduleDTO {
	private int schedule_idx;
	private int agreement_idx;
	private String name;
	private String content;
	@DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
	private Date start_date;
	@DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
	private Date end_date;
	private String location;
	private Date create_at;
}
