package com.dacoach.service.company;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dacoach.mapper.company.CompanyMapper;
import com.dacoach.model.company.CertDTO;
import com.dacoach.model.company.CompanyDTO;
import com.dacoach.model.company.CompanyProvideDTO;
import com.dacoach.model.company.CompanyRegionDTO;
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
	public int companyInfo(CompanyDTO dto) throws Exception {
		
		return companyMapper.companyInfo(dto);
	}
	public int insertcert(CertDTO dto) throws Exception{
		return companyMapper.insertcert(dto);
	}
	
	public int getCompanyNum(int idx) throws Exception{
		return companyMapper.getCompanyNum(idx);
		
	}
	public int addRegion(CompanyRegionDTO dto) throws Exception{
		return companyMapper.addRegion(dto);
	}
	public List<Map<String,Object>> getMinorField(int idx) throws Exception{
		return companyMapper.getMinorField(idx);
	}
	public int provideOk(CompanyProvideDTO dto) throws Exception{
		return companyMapper.provideOk(dto);
	}
}
