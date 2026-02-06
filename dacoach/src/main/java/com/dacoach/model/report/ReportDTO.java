package com.dacoach.model.report;

import java.sql.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter
@ToString
public class ReportDTO {
	
    private Integer report_idx;
	private Integer reporter_idx;
	private Integer reported_idx;
	private Integer reason_type_idx;
	private String content;
	private String status;
	private Date created_at;
	private Date updated_at;
	
}
