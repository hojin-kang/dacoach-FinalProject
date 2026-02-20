package com.dacoach.service.adminpolicy;

import java.util.*;

import com.dacoach.model.adminPolicy.PolicyDTO;

public interface AdminPolicyService {

	List<Map<String,Object>> getPolicyList(int start,int end);
	int getNoticeTotalCnt();
	int getNoticeAllCnt();
	void savePolicy(PolicyDTO policy);
	void deletePolicy(int qna_idx);
	PolicyDTO getLatestPolicy();
	List<Map<String, Object>> getNoticeList(Map<String, Object> param);
}
