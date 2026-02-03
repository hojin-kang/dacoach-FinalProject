package com.dacoach.model.qna;

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
public class Qna_aDTO {
	
	private int qna_a_idx;
    private int qna_idx;
    private String title;
    private String answer;
    private Date created_at;
}
