package com.dacoach.model.coach;

import java.sql.Date;

import com.dacoach.model.users.UsersDTO;

public class CoachDTO {
	private int coachIdx;
	private int usersIdx;
	private Date birthDate;
	private String nickName;
	private String phone;
	private String mail;
	private String intro;
	private String photo;
	private String video;
	
	private UsersDTO usersDto;
	
}
