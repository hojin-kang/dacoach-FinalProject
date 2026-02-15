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
	public List<Map<String,Object>> getPayHistory(int user_idx) throws Exception{
		return companyMapper.getPayHistory(user_idx);
	}
	@Override
	public int joinOk(UsersDTO dto) throws Exception {
		dto.setPassword(com.dacoach.javasecure.JavaDataSecureModule.getSHA256(dto.getPassword()));
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
	public CompanyDTO getCompanyInfo(int idx) throws Exception{
		return companyMapper.getCompanyInfo(idx);
	}
	public boolean regionCheck(int idx) throws Exception{
		
		List<CompanyRegionDTO> d=companyMapper.regionCheck(idx);
		boolean check=d==null||d.size()==0?false:true;
		return check;
	}
	
	@Override
	public boolean provideCheck(int idx) throws Exception {
		CompanyProvideDTO d=companyMapper.provideCheck(idx);
		boolean check=d==null?false:true;
		return check;
	}
	
	public List<Map<String,Object>> getCompanyRegion(int idx) throws Exception{
		return companyMapper.getCompanyRegion(idx);
	}
	public Map<String,Object> companyProfile(int member_idx) throws Exception{
		return companyMapper.companyProfile(member_idx);
	}
	public List<Map<String,Object>> getProfileClass(Map<String,Object> map)throws Exception{
		return companyMapper.getProfileClass(map);
	}
	@Override
	public CertDTO getCompanyCert(int user_idx) throws Exception {
		// TODO Auto-generated method stub
		return companyMapper.getCompanyCert(user_idx);
	}
	public Map<String,Object> getCompanyProvide(int company_idx) throws Exception{
		return companyMapper.getCompanyProvide(company_idx);
	}
	public List<CompanyRegionDTO> getCompanyAllRegion(int company_idx) throws Exception{
		return companyMapper.getCompanyAllRegion(company_idx);
	}
	
	public boolean companyUp(CompanyDTO companyDto,CompanyProvideDTO provideDto) throws Exception{
		boolean check=false;
		int comResult=companyMapper.companyUp(companyDto);
		if(comResult>0)check=true;
		
		if(provideDto.getMinor_field_idx()!=0) {
		int proResult=companyMapper.provideUp(provideDto);
		if(proResult>0)check=true;
		}
		
		return check;
	}
	public int regionDel(CompanyRegionDTO dto) throws Exception{
		return companyMapper.regionDel(dto);
	}
	public List<Map<String,Object>> getCoachList() throws Exception{
		return companyMapper.getCoachList();
	}
	
}
