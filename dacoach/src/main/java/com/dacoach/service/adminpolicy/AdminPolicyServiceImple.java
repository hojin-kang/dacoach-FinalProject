package com.dacoach.service.adminpolicy;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dacoach.mapper.adminPolicy.AdminPolicyMapper;
import com.dacoach.model.adminPolicy.PolicyDTO;

@Service
public class AdminPolicyServiceImple implements AdminPolicyService {

	@Autowired
	private AdminPolicyMapper mapper;

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
	public List<Map<String,Object>> getPolicyList(int start,int end) {
		return mapper.getPolicyList(start,end);
	}

	@Override
	public int getNoticeTotalCnt() {
		return mapper.getNoticeTotalCnt();
	}

	@Override
	public List<Map<String, Object>> getNoticeList(Map<String, Object>param) {
		return mapper.getNoticeList(param);
	}

	@Override
	public int getNoticeAllCnt() {
		return mapper.getNoticeAllCnt();
	}

}
