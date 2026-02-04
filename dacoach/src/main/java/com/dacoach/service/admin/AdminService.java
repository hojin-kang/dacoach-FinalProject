package com.dacoach.service.admin;

import java.util.*;

import com.dacoach.model.admin.EmbeddedUserDTO;


public interface AdminService {
	List<Map<String,Object>> getCoachList() throws Exception;
	public int getCertCount() throws Exception;
	List<Map<String,Object>> getCoachDetail(int coachidx) throws Exception;
	public String getCoachStatus(int userIdx) throws Exception;
	public EmbeddedUserDTO getCoachEmbedded(int userIdx) throws Exception;
	public int updateCoachSuspended(int userIdx) throws Exception;
	public int insertCoachSuspended(EmbeddedUserDTO dto) throws Exception;
}
