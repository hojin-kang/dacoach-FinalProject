package com.dacoach.mapper.adminPolicy;

import java.util.*;

import org.apache.ibatis.annotations.Mapper;

import com.dacoach.model.adminPolicy.PolicyDTO;

@Mapper
public interface AdminPolicyMapper {
    List<Map<String,Object>> getPolicyList(Map<String,Object>param);
    int insertPolicy(PolicyDTO policy);
    int updatePolicy(PolicyDTO policy); 
    int deletePolicy(int qna_idx);
    PolicyDTO getLatestPolicy();
    List<Map<String,Object>> getNoticeList(Map<String, Object>param);
    int getNoticeTotalCnt();
    int getNoticeAllCnt();
}
