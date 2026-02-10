package com.dacoach.model.company;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CompanyRegionDTO {

	private int company_idx;
	private int major_region_idx;
	private int minor_region_idx;
	private String region_detail;
	private String branch_name;
	private String branch_tel;
}