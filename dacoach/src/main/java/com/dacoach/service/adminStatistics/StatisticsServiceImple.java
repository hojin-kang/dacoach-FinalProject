package com.dacoach.service.adminStatistics;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dacoach.mapper.adminStatistics.StatisticsMapper;
import com.dacoach.model.admin.EmbeddedUserDTO;
import com.dacoach.model.coach.CoachDTO;
import com.dacoach.model.company.CertDTO;
import com.dacoach.model.qna.QnaDTO;
import com.dacoach.model.qna.Qna_aDTO;
import com.dacoach.model.report.ReportDTO;
import com.dacoach.model.users.UsersDTO;

@Service
public class StatisticsServiceImple implements StatisticsService {

	@Autowired
	private StatisticsMapper statisticsMapper;
	
	@Override
	public int monthlySales() throws Exception {
		Integer result=statisticsMapper.monthlySales();
		return (result==null)?0:result;
	}
	
	@Override
	public int lastMonthSales() throws Exception {
		Integer result=statisticsMapper.lastMonthSales();
		return (result==null)?0:result;
	}
	
	@Override
	public int monthlyMembershipSales() throws Exception {
		Integer result=statisticsMapper.monthlyMembershipSales();
		return (result==null)?0:result;
	}
	
	@Override
	public long avgPayAmount() throws Exception {
		return statisticsMapper.avgPayAmount();
	}
	
	@Override
	public int pendingRefundCount() throws Exception {
		return statisticsMapper.pendingRefundCount();
	}
	
	@Override
	public List<Map<String, Object>> weeklySales() throws Exception {
		return statisticsMapper.weeklySales();
	}
	
	@Override
	public List<Map<String, Object>> revenueByField() throws Exception {
		return statisticsMapper.revenueByField();
	}
	
	@Override
	public List<Map<String, Object>> payTypeStats() throws Exception {
		return statisticsMapper.payTypeStats();
	}
	
	@Override
	public int monthlyCancelAmount() throws Exception {
		Integer result=statisticsMapper.monthlyCancelAmount();
		return (result==null)?0:result;
	}

}
