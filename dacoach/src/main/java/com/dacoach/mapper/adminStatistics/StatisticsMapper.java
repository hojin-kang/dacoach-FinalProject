package com.dacoach.mapper.adminStatistics;

import java.util.*;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface StatisticsMapper {
	
	//통계관리 화면
	public Integer monthlySales() throws Exception;
	public Integer lastMonthSales() throws Exception;
	public Integer monthlyMembershipSales() throws Exception;
	public long avgPayAmount() throws Exception;
	public int pendingRefundCount() throws Exception;
	public List<Map<String,Object>> weeklySales() throws Exception;
	public List<Map<String,Object>> revenueByField() throws Exception;
	public List<Map<String,Object>> payTypeStats() throws Exception;
	public Integer monthlyCancelAmount() throws Exception;
	
}
