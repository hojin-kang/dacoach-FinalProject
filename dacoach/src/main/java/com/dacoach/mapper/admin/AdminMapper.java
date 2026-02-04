package com.dacoach.mapper.admin;

import java.util.*;

import org.apache.ibatis.annotations.Mapper;

import com.dacoach.model.admin.EmbeddedUserDTO;

@Mapper
public interface AdminMapper {
	public List<Map<String,Object>> getCoachList() throws Exception;
	public int getCertCount() throws Exception;
	public List<Map<String,Object>> getCoachDetail(int coach_idx) throws Exception;
	public String getCoachStatus(int user_idx) throws Exception;
	public EmbeddedUserDTO getCoachEmbedded(int user_idx) throws Exception;
	public int updateCoachStatus(Map<String, Object> params) throws Exception;
	public int insertCoachSuspended(EmbeddedUserDTO dto) throws Exception;
	public int updateEnddateSuspended(int user_idx) throws Exception;
}
