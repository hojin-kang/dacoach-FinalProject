package com.dacoach.service.company;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dacoach.mapper.company.CompanyMapper;
import com.dacoach.model.users.UsersDTO;

@Service
public class CompanyServiceImple implements CompanyService {

	@Autowired
	private CompanyMapper companyMapper;
	
	@Override
	public List<Map<String, Object>> fieldTeg() throws Exception{
		
		return companyMapper.fieldTag();
	}
	@Override
	public List<Map<String, Object>> regionTeg() throws Exception{
		
		return companyMapper.regionTag();
	}
	@Override
	public List<Map<String, Object>> getRegionTag(int idx) throws Exception {
		
		return companyMapper.getRegionTag(idx);
	}
	
	@Override
	public int joinOk(UsersDTO dto) throws Exception {

		int result=companyMapper.joinOk(dto);
		return  result;

	}
	@Override
	public int getUserIdx(String login_id) throws Exception {
		
		return companyMapper.getUserIdx(login_id);
	}
	@Override
	public int companyInfo(Map<String, Object> map) throws Exception {
		
		return companyMapper.companyInfo(map);
	}
	public int insertcert(Map<String, Object> map) throws Exception{
		return companyMapper.insertcert(map);
	}
}
