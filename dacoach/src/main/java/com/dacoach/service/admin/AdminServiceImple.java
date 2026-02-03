package com.dacoach.service.admin;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dacoach.mapper.admin.AdminMapper;

@Service
public class AdminServiceImple implements AdminService {

	@Autowired
	private AdminMapper adminMapper;
	
	@Override
	public List<Map<String, Object>> getCoachList() throws Exception {
		return adminMapper.getCoachList();
	}
	
	@Override
	public int getCertCount() throws Exception {
		return adminMapper.getCertCount();
	}
	
	@Override
	public List<Map<String, Object>> getCoachDetail(int coachidx) throws Exception {
		return adminMapper.getCoachDetail(coachidx);
	}

}
