package com.dacoach.mapper.admin;

import java.util.*;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AdminMapper {
	public List<Map<String,Object>> getCoachList() throws Exception;
	public int getCertCount() throws Exception;
	public List<Map<String,Object>> getCoachDetail(int coachIdx) throws Exception;
}
