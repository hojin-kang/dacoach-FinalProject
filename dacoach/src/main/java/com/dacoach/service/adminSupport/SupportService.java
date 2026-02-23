package com.dacoach.service.adminSupport;

import java.util.*;

import com.dacoach.model.qna.QnaDTO;
import com.dacoach.model.qna.Qna_aDTO;
import com.dacoach.model.report.ReportDTO;

public interface SupportService {
	
	//공지 관리(QnA 테이블 사용)
	public int getNoticeTotalCnt(String keyword) throws Exception;
	List<Map<String, Object>> getNoticeList(String keyword,int start,int end) throws Exception;
	public int insertNotice(QnaDTO dto) throws Exception;
	public QnaDTO getNoticeContent(int qna_idx) throws Exception;
	public int updateNotice(QnaDTO dto) throws Exception;
	public int deleteNotice(int qna_idx) throws Exception;
	
	//QnA 관리
	public int getQnaTotalCnt(String keyword) throws Exception;
	List<Map<String, Object>> getQnaList(String keyword,int start,int end) throws Exception;
	Map<String, Object> getQnaContent(int qna_idx) throws Exception;
	public int insertQnaAnswer(Qna_aDTO dto) throws Exception;
	public int updateQnaAnswer(Qna_aDTO dto) throws Exception;
	public int deleteQnaAnswer(int qna_a_idx) throws Exception;
	
	//신고관리
	public int getReportTotalCnt(String status) throws Exception;
	List<Map<String,Object>> reportList(String status,int start,int end) throws Exception;
	Map<String, Object> reportContent(int report_idx) throws Exception;
	public int getCoachIdx (int user_idx) throws Exception;
	public int getCompanyIdx (int user_idx) throws Exception;
	public int updateReport(ReportDTO dto) throws Exception;
	
}
