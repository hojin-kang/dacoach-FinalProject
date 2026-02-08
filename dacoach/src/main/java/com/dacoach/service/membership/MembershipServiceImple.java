package com.dacoach.service.membership;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dacoach.mapper.membership.MembershipMapper;

@Service
public class MembershipServiceImple implements MembershipService {
	
	@Autowired
	private MembershipMapper membership;
	@Override
	public int defaultMembership(int idx) throws Exception {
		
		return membership.defaultMembership(idx);
	}

}
