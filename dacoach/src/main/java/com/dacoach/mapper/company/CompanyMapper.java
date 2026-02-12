package com.dacoach.mapper.company;

import java.util.*;

import com.dacoach.model.company.CertDTO;
import com.dacoach.model.company.CompanyDTO;
import com.dacoach.model.company.CompanyProvideDTO;
import com.dacoach.model.company.CompanyRegionDTO;
import com.dacoach.model.users.UsersDTO;
public interface CompanyMapper {

	public List<Map<String, Object>> fieldTag() throws Exception;
	
	public List<Map<String, Object>> regionTag() throws Exception;
	
	public List<Map<String,Object>> getRegionTag(int idx) throws Exception;
	
	public List<Map<String,Object>> getMinorField(int idx) throws Exception;
	
	public int provideOk(CompanyProvideDTO dto) throws Exception;
	
	public int joinOk(UsersDTO dto) throws Exception;
	
	public int getUserIdx(String login_id) throws Exception;
	
	public int companyInfo(CompanyDTO dto) throws Exception;
	
	public int insertcert(CertDTO dto) throws Exception;
	
	public int getCompanyNum(int idx) throws Exception;
	
	public int addRegion(CompanyRegionDTO dto) throws Exception;
	
	public CompanyDTO getCompanyInfo(int idx) throws Exception;
	
	public List<CompanyRegionDTO> regionCheck(int idx) throws Exception;
	public CompanyProvideDTO provideCheck(int idx)	throws Exception;
	
	public List<Map<String,Object>> getCompanyRegion(int idx) throws Exception;
	
	public Map<String,Object> companyProfile(int member_idx) throws Exception;
	
	public List<Map<String,Object>> getProfileClass(Map<String,Object> map)throws Exception;
	
	public CertDTO getCompanyCert(int user_idx) throws Exception;
	
	public Map<String,Object> getCompanyProvide(int company_idx) throws Exception;
	
	public List<CompanyRegionDTO> getCompanyAllRegion(int company_idx) throws Exception;
 }
