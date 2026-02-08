package com.dacoach.model.membership;

import java.sql.*;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class MembershipDTO {
	
	private int member_idx;
	private int user_idx;
	private int member_detail_idx;
	private Date start_date;
	private Date end_date;
	private String status;
}
