package com.dacoach.service.company;

import com.dacoach.model.company.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dacoach.mapper.company.*;

@Service
public class CompanyServiceImple implements CompanyService {

	@Autowired
	private CompanyMapper companyMapper;
	

}
