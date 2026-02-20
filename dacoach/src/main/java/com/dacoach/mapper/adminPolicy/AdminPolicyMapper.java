package com.dacoach.mapper.adminPolicy;

import java.util.*;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.dacoach.model.adminPolicy.PolicyDTO;

@Mapper
public interface AdminPolicyMapper {
    List<Map<String,Object>> getPolicyList(int start,int end);
    int insertPolicy(PolicyDTO policy);
    int updatePolicy(PolicyDTO policy); 
    int deletePolicy(int qna_idx);
    PolicyDTO getLatestPolicy();
    List<Map<String,Object>> getNoticeList(@Param("type") String type,
    		@Param("startRow")int startRow,
    		@Param("endRow")int endRow);
    int getNoticeTotalCnt();
    int getNoticeAllCnt(String type);
}
