package com.dacoach.service.company;

import java.util.List;
import java.util.Map;

import com.dacoach.model.users.UsersDTO;

public interface CompanyService {
	
	public List<Map<String, Object>> fieldTeg() throws Exception;
	public List<Map<String, Object>> regionTeg() throws Exception;
	public List<Map<String, Object>> getRegionTag(int idx) throws Exception;
	public int joinOk(UsersDTO dto) throws Exception;
	public int getUserIdx(String login_id) throws Exception;
	public int companyInfo(Map<String,Object> map) throws Exception;
	public int insertcert(Map<String, Object> map) throws Exception;
}
