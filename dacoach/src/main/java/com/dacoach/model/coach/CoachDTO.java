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
	
	private long rating;
	private int review_count;
	
	private String chatStatus;  // 'NONE', 'APPLIED', 'RECEIVED', 'ACCEPTED'
    private String matchStatus; // 'NONE', 'APPLIED', 'RECEIVED', 'ACCEPTED'
    private int matchIdx;       // 매칭 수락/수정을 위한 PK

	private UsersDTO usersDto;
	private List<String> hashtags;
	private List<String> interhashtags;

}
