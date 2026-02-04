package com.dacoach.service.admin;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dacoach.mapper.admin.AdminMapper;
import com.dacoach.model.admin.EmbeddedUserDTO;

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
	public List<Map<String, Object>> getCoachDetail(int coach_idx) throws Exception {
		return adminMapper.getCoachDetail(coach_idx);
	}
	
	@Override
	public String getCoachStatus(int user_idx) throws Exception {
		return adminMapper.getCoachStatus(user_idx);
	}

	@Override
	public EmbeddedUserDTO getCoachEmbedded(int user_idx) throws Exception {
		return adminMapper.getCoachEmbedded(user_idx);
	}
	
	@Override
	public int updateCoachStatus(Map<String, Object> params) throws Exception {
		return adminMapper.updateCoachStatus(params);
	}
	
	@Override
	public int insertCoachSuspended(EmbeddedUserDTO dto) throws Exception {
		return adminMapper.insertCoachSuspended(dto);
	}
	
	@Override
	public int updateEnddateSuspended(int user_idx) throws Exception {
		return adminMapper.updateEnddateSuspended(user_idx);
	}

}
