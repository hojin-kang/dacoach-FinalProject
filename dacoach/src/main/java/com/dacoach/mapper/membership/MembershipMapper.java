package com.dacoach.mapper.membership;

import java.util.*;

import com.dacoach.model.membership.MembershipDTO;
import com.dacoach.model.membership.MembershipDetailDTO;
public interface MembershipMapper {
	
	public int defaultMembership(int idx) throws Exception;
	
	public MembershipDTO userMembershipInfo(int idx) throws Exception;
	
	public MembershipDetailDTO detailInfo(int detailIdx)throws Exception;
	
	public int membershipUpdate(MembershipDTO dto) throws Exception;
	
	public int membershipDown(MembershipDTO dto) throws Exception;
}
