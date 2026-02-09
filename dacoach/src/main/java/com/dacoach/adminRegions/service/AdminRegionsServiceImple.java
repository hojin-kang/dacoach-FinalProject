package com.dacoach.adminRegions.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dacoach.admin.regions.model.AdminRegionsDAO;

@Service
public class AdminRegionsServiceImple implements AdminRegionsService {

	@Autowired
	private AdminRegionsDAO dao;
	@Override
	public List<Map<String, Object>> getMajorRegions() {
		return dao.selectMajorRegions();
	}

}
