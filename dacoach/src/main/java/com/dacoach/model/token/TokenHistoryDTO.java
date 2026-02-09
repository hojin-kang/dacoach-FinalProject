package com.dacoach.model.token;

import java.sql.Date;

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
public class TokenHistoryDTO {
        
    private Integer user_idx;
	private String hist_type;
	private Integer amount;
	private Integer balance_after;
	private Date created_at;
}
