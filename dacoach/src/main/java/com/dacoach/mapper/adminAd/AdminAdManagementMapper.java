package com.dacoach.mapper.adminAd;

import org.apache.ibatis.annotations.Mapper;
import java.util.*;

@Mapper
public interface AdminAdManagementMapper {

	List<Map<String, Object>> getAdRequestList(Map<String,Object> param);

	int deleteAdRequest(int ad_idx);
	
	int getAdTotalCnt();
}
