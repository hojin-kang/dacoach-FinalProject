package com.dacoach.service.coach;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dacoach.mapper.coach.CoachMapper;
import com.dacoach.model.coach.CoachDTO;
import com.dacoach.model.users.UsersDTO;

@Service
@Transactional
public class CoachServiceImple implements CoachService {
	@Autowired
	private CoachMapper coachMapper;

	@Override
	public boolean idCheck(String username) throws Exception {
		boolean result=coachMapper.idCheck(username)>0?true:false;
		return result;
	}

	@Override
	public Integer coachProfile(UsersDTO udto) throws Exception {
		udto.setPassword(com.dacoach.javasecure.JavaDataSecureModule.getSHA256(udto.getPassword()));
		Integer result=coachMapper.coachProfile(udto);
		return result;
	}

	@Override
	public boolean checkNick(String nickname) throws Exception {
		boolean result=coachMapper.idCheck(nickname)>0?true:false;
		return result;
	}

	@Override
	public List<Map<String, Object>> getMajorFields() throws Exception {
		return coachMapper.getMajorFields();
	}

	@Override
	public List<Map<String, Object>> getMinorFields(int majorIdx) throws Exception {
		return coachMapper.getMinorFields(majorIdx);
	}

	@Override
	public List<Map<String, Object>> getMajorRegions() throws Exception {
		return coachMapper.getMajorRegions();
	}

	@Override
	public List<Map<String, Object>> getMinorRegions(int majorRegionIdx) throws Exception {
		return coachMapper.getMinorRegions(majorRegionIdx);
	}

	@Override
	public Integer getUsersIdx(String login_id) throws Exception {
		return coachMapper.getUsersIdx(login_id);
	}

	@Override
	public Integer coachJoin(CoachDTO cdto) throws Exception {
		return coachMapper.coachJoin(cdto);
	}

	@Override
	public CoachDTO getCoachInfo(int users_idx) throws Exception {
		
		return coachMapper.getCoachInfo(users_idx);
	}

	@Override
	public CoachDTO getCoachByKakaoKey(String kakaoKey) throws Exception {
		return coachMapper.getCoachByKakaoKey(kakaoKey);
	}

	@Override
	public int connectKakao(HashMap<String, Object> conKakao) throws Exception {
		return coachMapper.connectKakao(conKakao);
	}

	@Override
	public boolean emailCheck(String email) throws Exception {
		int result=coachMapper.emailCheck(email);
		return result>0?true:false;
	}

	@Override
	public Integer activateCoach(int user_idx) throws Exception {
		return coachMapper.activateCoach(user_idx);
	}

	@Override
	@Transactional
	public Integer saveCoachDetatils(int user_idx, int myMinorCate, int interMinorCate, int myMajorRegion,
			int myMinorRegion, String myHashtags, String interHashtags) throws Exception {
		int coach_idx=coachMapper.getCoachInfo(user_idx).getCoach_idx();
		//제공분야 및 활동지역
		HashMap map=new HashMap();
		map.put("coach_idx", coach_idx);
		map.put("myMinorCate", myMinorCate);
		map.put("interMinorCate", interMinorCate);
		map.put("myMajorRegion", myMajorRegion);
		map.put("myMinorRegion", myMinorRegion);
		coachMapper.saveMyMinorCate(map);
		coachMapper.saveInterMinorCate(map);
		coachMapper.saveMyRegions(map);
		//해시태그 저장
		String tags[]=myHashtags.split("#");
		for(int i=0;i<tags.length;i++) {
			if(!tags[i].trim().equals("")) {
				
			}
		}
		
		return 1;
	}

	

	
}
