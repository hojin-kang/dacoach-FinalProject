package com.dacoach.minorregion.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MinorRegionDTO {
	private int minor_region_idx;
	private String district_nm;
	private int major_region_idx;
}
