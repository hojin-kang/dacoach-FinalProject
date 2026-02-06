package com.dacoach.service.coachSearch;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dacoach.mapper.coachSearch.CoachSearchMapper;
import com.dacoach.model.coach.CoachDTO;
@Service
public class CoachSearchServiceImple implements CoachSearchService {
	@Autowired
	private CoachSearchMapper coachSearchMapper;

	@Override
	public List<CoachDTO> coachList(int cp, HashMap<String, Object> map) {
		map.put("start", (cp-1)*4+1);
		map.put("end", cp*4);
		List<CoachDTO> coachList = coachSearchMapper.coachList(map);
		return coachList;
	}

}
