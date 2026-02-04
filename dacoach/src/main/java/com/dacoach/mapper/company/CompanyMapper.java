package com.dacoach.mapper.company;

import java.util.*;

import com.dacoach.model.users.UsersDTO;
public interface CompanyMapper {

	public List<Map<String, Object>> fieldTag() throws Exception;
	
	public List<Map<String, Object>> regionTag() throws Exception;
	
	public List<Map<String,Object>> getRegionTag(int idx) throws Exception;
	
	public int joinOk(UsersDTO dto) throws Exception;
	
	public int getUserIdx(String login_id) throws Exception;
	
	public int companyInfo(Map<String, Object> map) throws Exception;
	
	public int insertcert(Map<String, Object> map) throws Exception;
 }
