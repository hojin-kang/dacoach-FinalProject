package com.dacoach.service.coach;

import java.lang.reflect.Array;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
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
	public void saveCoachDetails(int user_idx, int myMinorCate, int interMinorCate, int myMajorRegion,
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
		
		// 1. DB의 기존 태그 가져오기
		HashSet<String> allHashtags = new HashSet<>(coachMapper.allHashtags());

		// 2. mytags 처리
		HashSet<String> mytags = new HashSet<>();
		for (String s : myHashtags.split("#")) {
		    String t = s.trim();
		    if (!t.isEmpty()) mytags.add("#"+t); // 빈 값과 공백 제거 후 추가
		}
		
		HashSet<String> mytags_original = new HashSet<>(mytags); // 원본 복사본 생성
		mytags.removeAll(allHashtags);

		// 저장할 태그가 있을 때만 실행
		if (!mytags.isEmpty()) {
		    coachMapper.saveHashtags(mytags);
		    allHashtags.addAll(mytags); // 다음 비교를 위해 전체 목록 업데이트
		}
		// 3. intertags 처리
		HashSet<String> intertags = new HashSet<>();
		for (String s : interHashtags.split("#")) {
		    String t = s.trim();
		    if (!t.isEmpty()) intertags.add("#"+t);
		}
		HashSet<String> intertags_original = new HashSet<>(intertags); // 원본 복사본 생성
		intertags.removeAll(allHashtags);

		if (!intertags.isEmpty()) {
		    coachMapper.saveHashtags(intertags);
		}
		
		List<Integer> mytags_idx = coachMapper.getHashtagIdxs(mytags_original);
		List<Integer> intertags_idx = coachMapper.getHashtagIdxs(intertags_original);
		
		if(!mytags_idx.isEmpty()) {
			coachMapper.myHashtagMapping(user_idx, mytags_idx);
		}
		if(!intertags_idx.isEmpty()) {
			coachMapper.interHashtagMapping(user_idx, intertags_idx);
		}
		
	}

	
}
