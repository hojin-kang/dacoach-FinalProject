package com.dacoach.model.adminPolicy;

import java.sql.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PolicyDTO {

	private int qna_idx;  
	private String qna_type;
    private String title;        
    private String question;     
    private int user_idx;        
    private String login_id;  
    private Date created_at;
}
	