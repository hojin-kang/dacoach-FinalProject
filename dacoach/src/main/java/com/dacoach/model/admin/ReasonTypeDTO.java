package com.dacoach.model.admin;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter
@ToString
public class ReasonTypeDTO {

	// 소연 작업중
	private Integer reason_type_idx;
	private String type_name;
	private String content;
}
