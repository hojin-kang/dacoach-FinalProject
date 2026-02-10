package com.dacoach.mapper.adminPolicy;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.dacoach.model.adminPolicy.PolicyDTO;

@Mapper
public interface AdminPolicyMapper {
    List<PolicyDTO> getPolicyList();
    int insertPolicy(PolicyDTO policy);
    int updatePolicy(PolicyDTO policy); 
    int deletePolicy(int qna_idx);
    PolicyDTO getLatestPolicy();
}
