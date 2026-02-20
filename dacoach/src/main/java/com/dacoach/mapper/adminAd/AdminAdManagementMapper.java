package com.dacoach.mapper.adminAd;

import org.apache.ibatis.annotations.Mapper;
import java.util.*;

@Mapper
public interface AdminAdManagementMapper {

	List<Map<String, Object>> getAdRequestList(int startRow,int endRow);

	int deleteAdRequest(int ad_idx);
	
	int getAdTotalCnt();
}
