package com.dacoach.model.challenge;

import java.sql.Date;
import java.util.List;

import com.dacoach.model.users.UsersDTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ChallengeDTO {
	private int user_idx;
	
	private int challenge_idx;
	private String name;
	private String type;
	private int quantity;
	private int prize;
	
	private Date achieved_date;
	private String achieve;
}
