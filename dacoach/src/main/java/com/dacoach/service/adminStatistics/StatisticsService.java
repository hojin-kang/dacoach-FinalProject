package com.dacoach.service.adminStatistics;

import java.util.*;

public interface StatisticsService {
	
	//통계관리화면
	public int monthlySales() throws Exception;
	public int lastMonthSales() throws Exception;
	public int monthlyMembershipSales() throws Exception;
	public long avgPayAmount() throws Exception;
	public int pendingRefundCount() throws Exception;
	List<Map<String,Object>> weeklySales() throws Exception;
	List<Map<String,Object>> revenueByField() throws Exception;
	List<Map<String,Object>> payTypeStats() throws Exception;
	public int monthlyCancelAmount() throws Exception;
}
