package com.dacoach.service.users;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dacoach.mapper.users.UsersMapper;
import com.dacoach.model.users.UsersDTO;

@Service
public class UsersServiceImple implements UsersService {
	@Autowired
	private UsersMapper usersMapper;

	@Override
	public UsersDTO userLogin(UsersDTO dto) throws Exception {
	    // 1. DB에서 해당 아이디를 가진 유저 정보를 가져옵니다.
	    UsersDTO udto = usersMapper.userLogin(dto);
	    
	    if (udto == null) {
	        return null; 
	    }
	    
	    
	    String dbPwd = udto.getPassword();
	    String userPwd=com.dacoach.javasecure.JavaDataSecureModule.getSHA256(dto.getPassword());
	    
	    if (dbPwd != null && dbPwd.equalsIgnoreCase(userPwd)) {
	        return udto;
	    } else {
	        return null;
	    }
	}

}
