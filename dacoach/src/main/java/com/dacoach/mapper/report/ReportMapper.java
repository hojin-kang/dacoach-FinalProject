package com.dacoach.mapper.report;

import org.apache.ibatis.annotations.Mapper;

import com.dacoach.model.report.ReportDTO;

import java.util.*;

@Mapper
public interface ReportMapper {

	List<Map<String, Object>> getReportReasons();

    int addReport(ReportDTO dto);
    
}
