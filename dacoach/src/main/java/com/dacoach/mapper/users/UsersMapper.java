package com.dacoach.mapper.users;

import java.util.List;

import com.dacoach.model.users.UsersDTO;

public interface UsersMapper {
	public UsersDTO userLogin(UsersDTO dto) throws Exception;
	public List<UsersDTO> getSuspendedLogs(int user_idx) throws Exception;
	public int deleteUser(int user_idx) throws Exception;
	public List<UsersDTO> getExpiredLogs(int user_idx) throws Exception;
	public int changeStatusToActive(int user_idx) throws Exception;
	
}
