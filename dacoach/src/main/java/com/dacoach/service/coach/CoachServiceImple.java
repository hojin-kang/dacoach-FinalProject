package com.dacoach.service.coach;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dacoach.mapper.coach.CoachMapper;

@Service
public class CoachServiceImple implements CoachService {
	@Autowired
	private CoachMapper coachMapper;

	@Override
	public boolean idCheck(String username) throws Exception {
		boolean result=coachMapper.idCheck(username)>0?true:false;
		return result;
	}
}
