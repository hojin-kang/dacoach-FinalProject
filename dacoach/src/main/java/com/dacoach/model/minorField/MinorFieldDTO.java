package com.dacoach.model.minorField;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MinorFieldDTO {

	private Integer minor_field_idx;
	private Integer major_field_idx;
	private String minor_field_nm;
	
}
