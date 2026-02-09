package com.dacoach.service.membership;

import java.util.List;

import com.dacoach.model.company.AdDTO;
import com.dacoach.model.membership.MembershipDTO;
import com.dacoach.model.membership.MembershipDetailDTO;

public interface MembershipService {
	public int defaultMembership(int idx) throws Exception;
	
	public MembershipDTO userMembershipInfo(int idx) throws Exception;
	
	public MembershipDetailDTO detailInfo(int detailIdx)throws Exception;
	
	public MembershipDetailDTO detail(int detailIdx)throws Exception;
	
	public int membershipUpdate(MembershipDTO dto) throws Exception;

	public int membershipDown(MembershipDTO dto) throws Exception;
	
	public int autoUpdate(MembershipDTO dto) throws Exception;
	
	public int bannerAdd(AdDTO dto) throws Exception;
	
	public int bannerUp(AdDTO dto) throws Exception;
	
	public List<AdDTO> bannerSelect(Integer member_idx) throws Exception;
}
