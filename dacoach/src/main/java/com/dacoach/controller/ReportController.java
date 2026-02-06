package com.dacoach.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.dacoach.model.report.ReportDTO;
import com.dacoach.service.report.ReportService;
import java.util.*;

import jakarta.servlet.http.HttpSession;

@Controller
public class ReportController {

	@Autowired
	private ReportService reportService;
	
	@PostMapping("/report/form")
	public ModelAndView addReportForm(@RequestParam("reported_idx") Integer reported_idx, String returnUrl, HttpSession session) throws Exception {
		
		ModelAndView mav = new ModelAndView();
		
		Integer reporter_idx = (Integer) session.getAttribute("user_idx");
		if (reporter_idx == null) {
			mav.setViewName("redirect:/login");
			return mav;
		}
		
		// 본인 신고 방지(권장)
        if (reporter_idx.equals(reported_idx)) {
            mav.setViewName("redirect:/");
            return mav;
        }
        
        // 신고 사유 리스트 불러오기
        List<Map<String, Object>> reasons = reportService.getReportReasons();

        mav.addObject("reported_idx", reported_idx);
        mav.addObject("returnUrl", returnUrl);
        mav.addObject("reasons", reasons);

        mav.setViewName("coach/addReport");
        return mav;
	}
	
	@PostMapping("/report")
    public ModelAndView addReport(ReportDTO rdto, String returnUrl, HttpSession session) throws Exception {
        ModelAndView mav = new ModelAndView();

        Integer reporter_idx = (Integer) session.getAttribute("user_idx");
        if (reporter_idx == null) {
            mav.setViewName("redirect:/login");
            return mav;
        }

        // 조작 방지: 서버에서 주입
        rdto.setReporter_idx(reporter_idx);

        // 본인 신고 방지(권장)
        if (reporter_idx.equals(rdto.getReported_idx())) {
            mav.setViewName("redirect:/");
            return mav;
        }

        int result = reportService.addReport(rdto);
        if(result>0) {
        	mav.addObject("msg", "신고가 접수되었습니다.");
        	mav.addObject("url", returnUrl);
        	mav.setViewName("alert");
        } else {
        	mav.addObject("msg", "신고 오류:고객센터로 문의 바랍니다.");
        	mav.addObject("url", "coach/mypage/myQnaForm");
        	mav.setViewName("alert");
        }

        return mav;
    }
}
