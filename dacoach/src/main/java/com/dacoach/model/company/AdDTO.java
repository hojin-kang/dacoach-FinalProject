package com.dacoach.model.company;

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
public class AdDTO {
	
	private int ad_idx;
	private int member_idx;
	private String photo;
	private Date created_at;
}
