package com.dacoach.service.report;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dacoach.mapper.report.ReportMapper;
import com.dacoach.model.report.ReportDTO;

@Service
public class ReportServiceImple implements ReportService {

	@Autowired
	private ReportMapper reportMapper;
	
	@Override
	public List<Map<String, Object>> getReportReasons() throws Exception {
		return reportMapper.getReportReasons();
	}
	
	@Override
	public int addReport(ReportDTO dto) throws Exception {
		return reportMapper.addReport(dto);
	}
	
}
