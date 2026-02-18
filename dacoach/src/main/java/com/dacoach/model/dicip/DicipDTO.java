package com.dacoach.model.dicip;

import java.sql.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class DicipDTO {
	private int agreement_idx;
	private int writer_idx;
	private int receiver_idx;
	private String content;
	private String status;
	private Date created_at;
	
	private String writer_name;
	private String receiver_name;
}
