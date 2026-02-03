package com.dacoach.service.company;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dacoach.mapper.company.CompanyMapper;

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

}
