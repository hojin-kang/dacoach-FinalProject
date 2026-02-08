package com.dacoach.model.coach;

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
public class CoachDTO {
	private int coach_idx;
	private int user_idx;
	private Date birth_date;
	private String nickname;
	private String phone;
	private String mail;
	private String intro;
	private String photo;
	private String video;
	private int point_score;
	private int token_balance;
	private String kakao_key;

	private UsersDTO usersDto;
	private List<String> hashtags;

}
