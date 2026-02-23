package com.dacoach.service.adminSupport;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dacoach.mapper.adminSupport.SupportMapper;
import com.dacoach.model.qna.QnaDTO;
import com.dacoach.model.qna.Qna_aDTO;
import com.dacoach.model.report.ReportDTO;

@Service
public class SupportServiceImple implements SupportService {

	@Autowired
	private SupportMapper supportMapper;
	
	
	@Override
	public int getNoticeTotalCnt(String keyword) throws Exception {
		int count=supportMapper.getNoticeTotalCnt(keyword);
		return (count==0)?1:count;
	}
	
	@Override
	public List<Map<String, Object>> getNoticeList(String keyword,int start,int end) throws Exception {
		return supportMapper.getNoticeList(keyword,start,end);
	}
	
	@Override
	public int insertNotice(QnaDTO dto) throws Exception {
		return supportMapper.insertNotice(dto);
	}
	
	@Override
	public QnaDTO getNoticeContent(int qna_idx) throws Exception {
		return supportMapper.getNoticeContent(qna_idx);
	}
	
	@Override
	public int updateNotice(QnaDTO dto) throws Exception {
		return supportMapper.updateNotice(dto);
	}
	
	@Override
	public int deleteNotice(int qna_idx) throws Exception {
		return supportMapper.deleteNotice(qna_idx);
	}
	
	@Override
	public int getQnaTotalCnt(String keyword) throws Exception {
		int count=supportMapper.getQnaTotalCnt(keyword);
		return (count==0)?1:count;
	}
	
	@Override
	public List<Map<String, Object>> getQnaList(String keyword,int start,int end) throws Exception {
		return supportMapper.getQnaList(keyword,start,end);
	}
	
	@Override
	public Map<String, Object> getQnaContent(int qna_idx) throws Exception {
		return supportMapper.getQnaContent(qna_idx);
	}
	
	@Override
	public int insertQnaAnswer(Qna_aDTO dto) throws Exception {
		return supportMapper.insertQnaAnswer(dto);
	}
	
	@Override
	public int updateQnaAnswer(Qna_aDTO dto) throws Exception {
		return supportMapper.updateQnaAnswer(dto);
	}
	
	@Override
	public int deleteQnaAnswer(int qna_a_idx) throws Exception {
		return supportMapper.deleteQnaAnswer(qna_a_idx);
	}
	
	
	@Override
	public int getReportTotalCnt(String status) throws Exception {
		int count=supportMapper.getReportTotalCnt(status);
		return (count==0)?1:count;
	}
	
	@Override
	public List<Map<String, Object>> reportList(String status,int start,int end) throws Exception {
		return supportMapper.reportList(status,start,end);
	}
	
	@Override
	public Map<String, Object> reportContent(int report_idx) throws Exception {
		return supportMapper.reportContent(report_idx);
	}
	
	@Override
	public int getCoachIdx(int user_idx) throws Exception {
		return supportMapper.getCoachIdx(user_idx);
	}
	
	@Override
	public int getCompanyIdx(int user_idx) throws Exception {
		return supportMapper.getCompanyIdx(user_idx);
	}
	
	@Override
	public int updateReport(ReportDTO dto) throws Exception {
		return supportMapper.updateReport(dto);
	}

}
