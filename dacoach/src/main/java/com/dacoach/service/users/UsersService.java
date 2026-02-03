package com.dacoach.service.users;

import com.dacoach.model.users.UsersDTO;

public interface UsersService {
	public UsersDTO userLogin(UsersDTO dto) throws Exception;
}
