package com.dacoach.service.membership;

import java.util.List;

import com.dacoach.kakaopay.PayDTO;
import com.dacoach.model.admin.ReasonTypeDTO;
import com.dacoach.model.company.AdDTO;
import com.dacoach.model.membership.MembershipDTO;
import com.dacoach.model.membership.MembershipDetailDTO;

public interface MembershipService {
	public List<ReasonTypeDTO> downReason() throws Exception;
	
	public int defaultMembership(int idx) throws Exception;
	
	public MembershipDTO userMembershipInfo(int idx) throws Exception;
	
	public MembershipDetailDTO detailInfo(int detailIdx)throws Exception;
	
	public MembershipDetailDTO detail(int detailIdx)throws Exception;
	
	public int membershipUpdate(MembershipDTO dto) throws Exception;

	public int membershipDown(MembershipDTO dto) throws Exception;
	
	public int autoDown(int user_idx) throws Exception;
	
	public int bannerAdd(AdDTO dto) throws Exception;
	
	public int bannerUp(AdDTO dto) throws Exception;
	
	public List<AdDTO> bannerSelect(Integer member_idx) throws Exception;
	
	public List<MembershipDTO> autoUpdateTarget() throws Exception;
	
	public int autoUpdate(int user_idx) throws Exception;
	
	public PayDTO getPayInfo(int user_idx) throws Exception;
	
	public List<MembershipDTO> autoDownTarget() throws Exception;
	
	public int membershipContinue(int user_idx) throws Exception;
}
