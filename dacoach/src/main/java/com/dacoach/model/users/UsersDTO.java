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
	private int user_idx;
	private String user_type;
	private String user_name;
	private String login_id;
	private String password;
	private String status;
	private Date created_at;
}
