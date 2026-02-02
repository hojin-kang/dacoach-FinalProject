package com.dacoach.mapper.coach;

import com.dacoach.model.users.UsersDTO;

public interface CoachMapper {
	public Integer idCheck(String username) throws Exception;
	public Integer coachProfile(UsersDTO udto) throws Exception;
}
