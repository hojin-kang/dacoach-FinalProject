package com.dacoach.mapper.admin;

import java.util.*;

import org.apache.ibatis.annotations.Mapper;

import com.dacoach.model.admin.EmbeddedUserDTO;

@Mapper
public interface AdminMapper {
	public List<Map<String,Object>> getCoachList() throws Exception;
	public int getCertCount() throws Exception;
	public List<Map<String,Object>> getCoachDetail(int coachIdx) throws Exception;
	public String getCoachStatus(int userIdx) throws Exception;
	public EmbeddedUserDTO getCoachEmbedded(int userIdx) throws Exception;
	public int updateCoachSuspended(int userIdx) throws Exception;
	public int insertCoachSuspended(EmbeddedUserDTO dto) throws Exception;
}
