package com.dacoach.service.coach;

import com.dacoach.model.users.UsersDTO;

public interface CoachService {
	public boolean idCheck(String username) throws Exception;
	public Integer coachProfile(UsersDTO udto) throws Exception;
}
