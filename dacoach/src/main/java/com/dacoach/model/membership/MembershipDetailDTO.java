package com.dacoach.model.membership;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class MembershipDetailDTO {
	
	private int member_detail_idx;
	private String member_type;
	private int cost;
	private String benefit;
	private int class_max_cnt;
}
