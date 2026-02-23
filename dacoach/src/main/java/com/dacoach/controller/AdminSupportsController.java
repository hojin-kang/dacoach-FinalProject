package com.dacoach.controller;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.dacoach.model.notification.NotificationDTO;
import com.dacoach.model.qna.QnaDTO;
import com.dacoach.model.qna.Qna_aDTO;
import com.dacoach.model.report.ReportDTO;
import com.dacoach.service.adminSupport.SupportService;
import com.dacoach.service.notification.NotificationService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/admin")
public class AdminSupportsController {

	@Autowired
	private SupportService supportService;
	
	@Autowired
	private NotificationService notificationService;

	
	//공지사항
	@GetMapping("/support/notice")
	public String noticeList(Model model,
			@RequestParam(value="keyword", required=false) String keyword,
			@RequestParam(value="cp", defaultValue="1") int cp,
			HttpSession session) {
		
		if(session.getAttribute("loginAdmin")==null) {
			return "redirect:/admin";
		}
		
		int listSize=10;
		int pageSize=5;
		int noticeTotalCnt=0;
		List<Map<String, Object>> noticeList = new ArrayList<>();
		
		try {
			noticeTotalCnt=supportService.getNoticeTotalCnt(keyword);
			
			int start = (cp - 1) * listSize + 1;
			int end = cp * listSize;
			
			noticeList = supportService.getNoticeList(keyword,start,end);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		String url = "notice";
		String pageStr = "";

		if (keyword != null && !keyword.isEmpty()) {
		    url += "?keyword=" + keyword;
		    pageStr = com.dacoach.page.PageModule.makePagewithParams(url, noticeTotalCnt, listSize, pageSize, cp);
		} else {
		    pageStr = com.dacoach.page.PageModule.makePage(url, noticeTotalCnt, listSize, pageSize, cp);
		}
		
		model.addAttribute("noticeList", noticeList);
		model.addAttribute("keyword", keyword);
		model.addAttribute("pageStr",pageStr);
		model.addAttribute("contentPage", "admin/support/notice/noticeList");
		model.addAttribute("contentFragment", "noticeList");
		
		return "admin/dashboard";
	}
	
	@GetMapping("/support/notice/write")
	public String noticeWrite(Model model, HttpSession session) {
		
		if(session.getAttribute("loginAdmin")==null) {
			return "redirect:/admin";
		}
		
		model.addAttribute("contentPage", "admin/support/notice/noticeWrite");
		model.addAttribute("contentFragment", "noticeForm");
		
		return "admin/dashboard";
	}
	
	@PostMapping("/support/notice/insert")
	public String noticeInsert(QnaDTO dto,
			RedirectAttributes rttr, HttpSession session) {
		
		if(session.getAttribute("loginAdmin")==null) {
			return "redirect:/admin";
		}
		
		try {
			int result = supportService.insertNotice(dto);
			
			if(result > 0) {
				rttr.addFlashAttribute("msg", "공지사항이 등록되었습니다.");
			}else {
				rttr.addFlashAttribute("msg", "공지사항 등록에 실패했습니다.");
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			rttr.addFlashAttribute("msg", "시스템 오류가 발생했습니다: " + e.getMessage());
		}
		
		return "redirect:/admin/support/notice";
	}
	
	@GetMapping("/support/notice/content")
	public String noticeContent(Model model, @RequestParam int qna_idx,
			HttpSession session) {
		
		if(session.getAttribute("loginAdmin")==null) {
			return "redirect:/admin";
		}
		
		QnaDTO dto = new QnaDTO();
		
		try {
			dto = supportService.getNoticeContent(qna_idx);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		model.addAttribute("content", dto);
		model.addAttribute("contentPage", "admin/support/notice/noticeContent");
		model.addAttribute("contentFragment", "noticeContent");
		return "admin/dashboard";
	}
	
	@GetMapping("/support/notice/noticeUpdateForm")
	public String noticeUpdateForm(Model model, @RequestParam int qna_idx,
			HttpSession session) {
		
		if(session.getAttribute("loginAdmin")==null) {
			return "redirect:/admin";
		}
		
		QnaDTO dto = new QnaDTO();
		
		try {
			dto = supportService.getNoticeContent(qna_idx);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		model.addAttribute("content", dto);
		model.addAttribute("contentPage", "admin/support/notice/noticeWrite");
		model.addAttribute("contentFragment", "noticeForm");
		return "admin/dashboard";
	}
	
	@PostMapping("/support/notice/update")
	public String updateNotice(QnaDTO dto,
			RedirectAttributes rttr, HttpSession session) {
		
		if(session.getAttribute("loginAdmin")==null) {
			return "redirect:/admin";
		}
		
		try {
			int result = supportService.updateNotice(dto);
			
			if(result > 0) {
				rttr.addFlashAttribute("msg", "공지사항이 수정되었습니다.");
				return "redirect:/admin/support/notice";
			}else {
				rttr.addFlashAttribute("msg", "공지사항 수정에 실패했습니다.");
				return "redirect:/admin/support/notice/noticeUpdateForm?qna_idx=" + dto.getQna_idx();
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			rttr.addFlashAttribute("msg", "시스템 오류가 발생했습니다: " + e.getMessage());
			return "redirect:/admin/support/notice/noticeUpdateForm?qna_idx=" + dto.getQna_idx();
		}
		
	}
	
	@PostMapping("/support/notice/delete")
	public String deleteNotice(@RequestParam int qna_idx,
			RedirectAttributes rttr, HttpSession session) {
		
		if(session.getAttribute("loginAdmin")==null) {
			return "redirect:/admin";
		}
		
		try {
			int result = supportService.deleteNotice(qna_idx);
			
			if(result > 0) {
				rttr.addFlashAttribute("msg", "공지사항이 삭제되었습니다.");
			}else {
				rttr.addFlashAttribute("msg", "공지사항 삭제에 실패했습니다.");
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			rttr.addFlashAttribute("msg", "시스템 오류가 발생했습니다: " + e.getMessage());
		}
		
		return "redirect:/admin/support/notice";
	}

	//Qna
	@GetMapping("/support/qna")
	public String qnaList(Model model,
			@RequestParam(value="keyword", required=false) String keyword,
			@RequestParam(value="cp", defaultValue="1") int cp,
			HttpSession session) {
		
		if(session.getAttribute("loginAdmin")==null) {
			return "redirect:/admin";
		}
		
		int listSize=10;
		int pageSize=5;
		int qnaTotalCnt=0;
		List<Map<String, Object>> qnaList = new ArrayList<>();
			
		try {
			qnaTotalCnt=supportService.getQnaTotalCnt(keyword);
			
			int start = (cp - 1) * listSize + 1;
			int end = cp * listSize;
			
			qnaList = supportService.getQnaList(keyword,start,end);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		String url="qna";
		String pageStr="";
		
		if(keyword != null && !keyword.isEmpty()) {
			url += "?keyword=" + keyword;
			pageStr = com.dacoach.page.PageModule.makePagewithParams(url, qnaTotalCnt, listSize, pageSize, cp);
		} else {
			pageStr = com.dacoach.page.PageModule.makePage(url, qnaTotalCnt, listSize, pageSize, cp);
		}
				
		model.addAttribute("qnaList", qnaList);
		model.addAttribute("keyword", keyword);
		model.addAttribute("pageStr", pageStr);
		model.addAttribute("contentPage", "admin/support/qna/qnaList");
		model.addAttribute("contentFragment", "qnaList");
		
		return "admin/dashboard";
	}
	
	@GetMapping("/support/qna/content")
	public String qnaContent(Model model,@RequestParam int qna_idx, 
			HttpSession session) {
		
		if(session.getAttribute("loginAdmin")==null) {
			return "redirect:/admin";
		}
		
		Map<String, Object> qnaContent = null;
		
		try {
			qnaContent = supportService.getQnaContent(qna_idx);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		model.addAttribute("content", qnaContent);
		model.addAttribute("contentPage", "admin/support/qna/qnaContent");
		model.addAttribute("contentFragment", "qnaContent");
		
		return "admin/dashboard";
	}
	
	@PostMapping("/support/qna/insert")
	public String insertQnaAnswer(@RequestParam int qna_idx,
			@RequestParam String title,
			@RequestParam String answer,
			RedirectAttributes rttr,
			HttpSession session) {
		
		if(session.getAttribute("loginAdmin")==null) {
			return "redirect:/admin";
		}
		
		Qna_aDTO dto = new Qna_aDTO();
		dto.setQna_idx(qna_idx);
		dto.setTitle(title);
		dto.setAnswer(answer);
		
		try {
			int result = supportService.insertQnaAnswer(dto);
			
			if(result > 0) {
				rttr.addFlashAttribute("msg", "답변이 등록되었습니다.");
	
			}else {
				rttr.addFlashAttribute("msg", "답변 등록에 실패했습니다.");
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			rttr.addFlashAttribute("msg", "시스템 오류가 발생했습니다: " + e.getMessage());
		}
		
		return "redirect:/admin/support/qna/content?qna_idx=" + qna_idx;
		
	}
	
	@PostMapping("/support/qna/update")
	public String updateQnaAnswer(@RequestParam int qna_a_idx,
			@RequestParam int qna_idx,
			@RequestParam String title,
			@RequestParam String answer,
			RedirectAttributes rttr,
			HttpSession session) {
		
		if(session.getAttribute("loginAdmin")==null) {
			return "redirect:/admin";
		}
		
		Qna_aDTO dto = new Qna_aDTO();
		dto.setQna_a_idx(qna_a_idx);
		dto.setTitle(title);
		dto.setAnswer(answer);
		
		try {
			int result = supportService.updateQnaAnswer(dto);
			
			if(result > 0) {
				rttr.addFlashAttribute("msg", "답변이 수정되었습니다.");
	
			}else {
				rttr.addFlashAttribute("msg", "답변 수정에 실패했습니다.");
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			rttr.addFlashAttribute("msg", "시스템 오류가 발생했습니다: " + e.getMessage());
		}
		
		return "redirect:/admin/support/qna/content?qna_idx=" + qna_idx;
	}
	
	@PostMapping("/support/qna/delete")
	public String deleteQnaAnswer(@RequestParam int qna_a_idx,
			@RequestParam int qna_idx,
			RedirectAttributes rttr,
			HttpSession session) {
		
		if(session.getAttribute("loginAdmin")==null) {
			return "redirect:/admin";
		}
		
		try {
			int result = supportService.deleteQnaAnswer(qna_a_idx);
			
			if(result > 0) {
				rttr.addFlashAttribute("msg", "답변이 삭제되었습니다.");
	
			}else {
				rttr.addFlashAttribute("msg", "답변 삭제에 실패했습니다.");
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			rttr.addFlashAttribute("msg", "시스템 오류가 발생했습니다: " + e.getMessage());
		}
		
		return "redirect:/admin/support/qna/content?qna_idx=" + qna_idx;
	}
	
	
	//신고관리
	@GetMapping("/report")
	public String reportList(Model model, HttpSession session,
			@RequestParam(value="status", required=false) String status,
			@RequestParam(value="cp", defaultValue="1")int cp) {
		
		if(session.getAttribute("loginAdmin")==null) {
			return "redirect:/admin";
		}
		
		int listSize=10;
		int pageSize=5;
		int reportTotalCnt = 0;
		List<Map<String, Object>> reportList = new ArrayList<>();
		
		try {
			reportTotalCnt=supportService.getReportTotalCnt(status);
			
			int start = (cp - 1) * listSize + 1;
			int end = cp * listSize;
			
			reportList = supportService.reportList(status, start, end);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		String url="report";
		String pageStr="";
		if(status != null && !status.isEmpty()) {
			url += "?status=" + status;
			pageStr = com.dacoach.page.PageModule.makePagewithParams(url, reportTotalCnt, listSize, pageSize, cp);
		} else {
			pageStr = com.dacoach.page.PageModule.makePage(url, reportTotalCnt, listSize, pageSize, cp);
		}
		
		model.addAttribute("reportList",reportList);
		model.addAttribute("selectedStatus", status);
		model.addAttribute("pageStr",pageStr);
		
		model.addAttribute("contentPage", "admin/report/reportList");
		model.addAttribute("contentFragment", "reportList");

		return "admin/dashboard";
		
	}
	
	
	@GetMapping("/report/content")
	public String reportContent(Model model,
			@RequestParam int report_idx,
			HttpSession session) {
		
		if(session.getAttribute("loginAdmin")==null) {
			return "redirect:/admin";
		}
		
		Map<String, Object> reportContent = new HashMap<>();
		
		try {
			reportContent=supportService.reportContent(report_idx);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		model.addAttribute("report",reportContent);
		model.addAttribute("contentPage", "admin/report/reportDetail");
		model.addAttribute("contentFragment", "reportDetail");

		return "admin/dashboard";
		
	}
	

	@PostMapping("/report/update")
	public String updateReport(
			@RequestParam int report_idx,
			@RequestParam String status,
			@RequestParam(required = false) Integer reporter_idx,
			@RequestParam(required = false) Integer reported_idx,
			@RequestParam String content,
			HttpSession session) {
		
		if(session.getAttribute("loginAdmin")==null) {
			return "redirect:/admin";
		}
		
		ReportDTO rdto=new ReportDTO();
		rdto.setStatus(status);
		rdto.setReport_idx(report_idx);
		
		
		try {
			int result=supportService.updateReport(rdto);
			
			if(result>0) {
				List<Integer> receiver = new ArrayList<>();
				if (reporter_idx != null) receiver.add(reporter_idx);
				if (reported_idx != null) receiver.add(reported_idx);
				
				for(Integer receiver_idx : receiver) {
					NotificationDTO ndto = new NotificationDTO();
					ndto.setReceiver_idx(receiver_idx);
					ndto.setProvider_idx(1);
					ndto.setNoti_type(String.valueOf(report_idx));
					ndto.setContent(content);
					notificationService.insertNotification(ndto);
				}
				
			}
			return "redirect:/admin/report";
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "redirect:/admin/report/content?report_idx=" + report_idx;
		}
		
	}
	
	
	
}
