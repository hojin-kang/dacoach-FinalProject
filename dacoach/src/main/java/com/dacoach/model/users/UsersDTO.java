package com.dacoach.model.users;

import java.sql.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
public class UsersDTO {
	private int usersIdx;
	private String userType;
	private String userName;
	private String loginId;
	private String password;
	private String status;
	private Date createdAt;
}
