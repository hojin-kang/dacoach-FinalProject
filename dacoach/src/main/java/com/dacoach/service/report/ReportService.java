package com.dacoach.service.report;

import java.util.*;

import com.dacoach.model.report.ReportDTO;

public interface ReportService {

	List<Map<String, Object>> getReportReasons() throws Exception;

    int addReport(ReportDTO dto) throws Exception;
}
