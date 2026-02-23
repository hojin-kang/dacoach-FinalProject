package com.dacoach.controller;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.RequestMapping;

import com.dacoach.service.adminStatistics.StatisticsService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/admin")
public class AdminStatisticsController {

	@Autowired
	private StatisticsService statisticsService;
	
	//통계관리
	@GetMapping("/statistics")
	public String statisticsList(Model model, HttpSession session) {
		
		if(session.getAttribute("loginAdmin")==null) {
			return "redirect:/admin";
		}
		
		int monthlySales=0;
		int lastMonthSales=0;
		int monthlyMembershipSales=0;
		long avgPayAmount=0;
		int pendingRefundCount=0;
		double growth=0.0;
		List<Map<String, Object>> weeklySales = new ArrayList<>();
		List<Map<String, Object>> revenueByField = new ArrayList<>();
		List<Map<String, Object>> payTypeStats = new ArrayList<>();
		int monthlyCancelAmount=0;
		
		try {
			// 성장률 공식: (이번달 - 지난달) / 지난달 * 100
			monthlySales=statisticsService.monthlySales();
			lastMonthSales=statisticsService.lastMonthSales();
			monthlyMembershipSales=statisticsService.monthlyMembershipSales();
			avgPayAmount=statisticsService.avgPayAmount();
			pendingRefundCount=statisticsService.pendingRefundCount();
			weeklySales = statisticsService.weeklySales();
			revenueByField = statisticsService.revenueByField();
			payTypeStats = statisticsService.payTypeStats();
			monthlyCancelAmount = statisticsService.monthlyCancelAmount();
			
			if(lastMonthSales != 0) {
				growth = 100.0 * (monthlySales - lastMonthSales) / lastMonthSales;
			}else if (lastMonthSales == 0 && monthlySales > 0){
				growth = 100.0;
			}else {
				growth = 0.0;
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		model.addAttribute("monthlySales",monthlySales);
		model.addAttribute("growth",growth);
		model.addAttribute("membershipRevenue",monthlyMembershipSales);
		model.addAttribute("avgPayAmount",avgPayAmount);
		model.addAttribute("pendingRefundCount",pendingRefundCount);
		model.addAttribute("weeklySales", weeklySales);
		model.addAttribute("revenueByField",revenueByField);
		model.addAttribute("payTypeStats",payTypeStats);
		model.addAttribute("monthlyCancelAmount",monthlyCancelAmount);
		model.addAttribute("contentPage", "admin/revenue/statisticsList");
		model.addAttribute("contentFragment", "statisticsContent");

		return "admin/dashboard";
		
	}

	
}
