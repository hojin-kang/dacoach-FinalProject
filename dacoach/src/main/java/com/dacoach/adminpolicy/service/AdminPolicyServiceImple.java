package com.dacoach.adminpolicy.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dacoach.mapper.adminPolicy.AdminPolicyMapper;
import com.dacoach.model.adminPolicy.PolicyDTO;

@Service
public class AdminPolicyServiceImple implements AdminPolicyService {

	@Autowired
	private AdminPolicyMapper mapper;
	
	@Override
	public List<PolicyDTO> getPolicyList() {
		return mapper.getPolicyList();
	}

	@Override
	public void savePolicy(PolicyDTO policy) {
		if(policy.getQna_idx() == 0) {
			mapper.insertPolicy(policy);
		} else {
			mapper.updatePolicy(policy);
		}
	}

	@Override
	public void deletePolicy(int qna_idx) {
		mapper.deletePolicy(qna_idx);
	}

	@Override
	public PolicyDTO getLatestPolicy() {
		return mapper.getLatestPolicy();
	}

	@Override
	public List<PolicyDTO> getNoticeList() {
		return mapper.getNoticeList();
	}

}
