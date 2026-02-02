package com.dacoach.service.coach;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dacoach.mapper.coach.CoachMapper;
import com.dacoach.model.users.UsersDTO;

@Service
public class CoachServiceImple implements CoachService {
	@Autowired
	private CoachMapper coachMapper;

	@Override
	public boolean idCheck(String username) throws Exception {
		boolean result=coachMapper.idCheck(username)>0?true:false;
		return result;
	}

	@Override
	public Integer coachProfile(UsersDTO udto) throws Exception {
		udto.setPassword(com.dacoach.javasecure.JavaDataSecureModule.getSHA256(udto.getPassword()));
		Integer result=coachMapper.coachProfile(udto);
		return result;
	}
}
