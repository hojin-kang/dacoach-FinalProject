package com.dacoach.service.membership;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dacoach.mapper.membership.MembershipMapper;
import com.dacoach.model.membership.MembershipDTO;
import com.dacoach.model.membership.MembershipDetailDTO;

@Service
public class MembershipServiceImple implements MembershipService {
	
	@Autowired
	private MembershipMapper membership;
	@Override
	public int defaultMembership(int idx) throws Exception {
		
		return membership.defaultMembership(idx);
	}
	@Override
	public MembershipDTO userMembershipInfo(int idx) throws Exception {
		// TODO Auto-generated method stub
		return membership.userMembershipInfo(idx);
	}
	
	@Override
	public MembershipDetailDTO detailInfo(int detailIdx) throws Exception {
		// TODO Auto-generated method stub
		return membership.detailInfo(detailIdx);
	}
	
	public int membershipUpdate(MembershipDTO dto) throws Exception{
		return membership.membershipUpdate(dto);
	}

}
