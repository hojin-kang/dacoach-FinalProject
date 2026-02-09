package com.dacoach.service.coachSearch;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dacoach.mapper.coach.CoachMapper;
import com.dacoach.mapper.coachSearch.CoachSearchMapper;
import com.dacoach.model.coach.CoachDTO;

@Service
@Transactional
public class CoachSearchServiceImple implements CoachSearchService {
    
    @Autowired
    private CoachSearchMapper coachSearchMapper;
    @Autowired
    private CoachMapper coachMapper;

    @Override
    public List<CoachDTO> coachList(int cp, HashMap<String, Object> map) {
        map.put("start", (cp - 1) * 4 + 1);
        map.put("end", cp * 4);
        return coachSearchMapper.coachList(map);
    }

    @Override
    public List getCoachHashtags(int user_idx) throws Exception {
        return coachSearchMapper.getCoachHashtags(user_idx);
    }
	@Override
	public List getInterHashtags(int user_idx) throws Exception {
		return coachSearchMapper.getInterHashtags(user_idx);
	}

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void applyMatchOrChat(int me, int target, String type) throws Exception {
        Map<String, Object> map = new HashMap<>();
        map.put("me", me);
        map.put("target", target);
        map.put("type", type);

        // 1. MATCH 테이블 상태 업데이트 (또는 삽입)
        int matchResult = coachSearchMapper.upsertMatchRequest(map);
        CoachDTO coach=coachMapper.getCoachInfo(me);
        
        if (matchResult <= 0) {
            throw new Exception("신청 처리 중 오류가 발생했습니다.");
        }

        // 2. 알림 내용 설정
        String typeName = type.equals("CHAT") ? "채팅" : "매칭";
        map.put("notiType", type + "_REQUEST");
        map.put("content", coach.getNickname()+"님으로 부터 새로운 " + typeName + " 신청이 도착했습니다!");

        // 3. NOTIFICATION 테이블 삽입
        coachSearchMapper.insertNotification(map);
    }

    @Override
    public CoachDTO getCoachDetailStatus(int target_idx, int login_idx) {
        Map<String, Object> params = new HashMap<>();
        params.put("target_idx", target_idx);
        params.put("login_idx", login_idx);
        
        return coachSearchMapper.getCoachDetailStatus(params);
    }

	@Override
	public Integer useTokens(int user_idx, int amount) throws Exception {
		HashMap<String, Object> map = new HashMap<>();
		map.put("user_idx", user_idx);
		map.put("amount", amount);
		return coachSearchMapper.useTokens(map);
	}


}