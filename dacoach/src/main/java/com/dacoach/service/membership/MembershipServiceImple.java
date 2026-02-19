package com.dacoach.service.membership;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dacoach.kakaopay.PayDTO;
import com.dacoach.mapper.membership.MembershipMapper;
import com.dacoach.model.company.AdDTO;
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
	
	public MembershipDetailDTO detail(int detailIdx)throws Exception{
		return membership.detail(detailIdx);
	}
	
	public int membershipUpdate(MembershipDTO dto) throws Exception{
		return membership.membershipUpdate(dto);
	}
	
	@Override
	public int membershipDown(MembershipDTO dto) throws Exception {
		// TODO Auto-generated method stub
		return membership.membershipDown(dto);
	}
	
	public int autoDown(int user_idx) throws Exception{
		
	return membership.autoDown(user_idx);
	}
	
	public int bannerAdd(AdDTO dto) throws Exception{
		return membership.bannerAdd(dto);
	}
	
	public int bannerUp(AdDTO dto) throws Exception{
		return membership.bannerUp(dto);
	}
	public List<AdDTO> bannerSelect(Integer member_idx) throws Exception{
		return membership.bannerSelect(member_idx);
	}
	
	public List<MembershipDTO> autoUpdateTarget() throws Exception{
		return membership.autoUpdateTarget();
	}
	public int autoUpdate(int user_idx) throws Exception{
		return membership.autoUpdate(user_idx);
	}
	
	public PayDTO getPayInfo(int user_idx) throws Exception{
		return membership.getPayInfo(user_idx);
	}
	
	public List<MembershipDTO> autoDownTarget() throws Exception{
		return membership.autoDownTarget();
	}
}
