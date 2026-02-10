package com.dacoach.adminpolicy.service;

import java.util.List;

import com.dacoach.model.adminPolicy.PolicyDTO;

public interface AdminPolicyService {

	List<PolicyDTO> getPolicyList();
	void savePolicy(PolicyDTO policy);
	void deletePolicy(int qna_idx);
	PolicyDTO getLatestPolicy();
}
