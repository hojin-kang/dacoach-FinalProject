package com.dacoach.mapper.users;

import com.dacoach.model.users.UsersDTO;

public interface UsersMapper {
	public UsersDTO userLogin(UsersDTO dto) throws Exception;
	
}
