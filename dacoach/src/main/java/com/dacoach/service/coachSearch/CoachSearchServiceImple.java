package com.dacoach.service.coachSearch;

import java.util.*;

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
        
        List<CoachDTO> list = coachSearchMapper.coachList(map);

        if (list != null) {
            for (CoachDTO c : list) {

                // 해시태그 최대 2개 제한
                List<String> my = coachSearchMapper.getCoachHashtags(c.getUser_idx());
                List<String> inter = coachSearchMapper.getInterHashtags(c.getUser_idx());

                if (my != null && my.size() > 2) my = my.subList(0, 2);
                if (inter != null && inter.size() > 2) inter = inter.subList(0, 2);

                c.setHashtags(my);
                c.setInterhashtags(inter);
            }
        }

        return list;
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

	@Override
	public Integer addTokenHistory(Integer user_idx, String hist_type, Integer amount, Integer balance_after) throws Exception {
		Map<String, Object> thmap = new HashMap<>();
	    thmap.put("user_idx", user_idx);
	    thmap.put("hist_type", hist_type);
	    thmap.put("amount", amount);
	    thmap.put("balance_after", balance_after);
		coachSearchMapper.addTokenHistory(thmap);
		return null;
	}

	@Override
	public Integer acceptChat(int user_idx, int target_idx) throws Exception {
		HashMap<String, Object> map = new HashMap<>();
		map.put("me", user_idx);
		map.put("target", target_idx);
		Integer result=coachSearchMapper.acceptChat(map);
		if(result>0) {
			CoachDTO coach=coachMapper.getCoachInfo(user_idx);
	        // 2. 알림 내용 설정
	        map.put("notiType", "CHAT_ACCEPT");
	        map.put("content", coach.getNickname()+"님이 채팅을 수락했습니다.");

	        // 3. NOTIFICATION 테이블 삽입
	        coachSearchMapper.insertNotification(map);
		}
		
		return result;
	}

	@Override
	public Integer acceptMatch(int user_idx, int target_idx) throws Exception {
		HashMap<String, Object> map = new HashMap<>();
		map.put("me", user_idx);
		map.put("target", target_idx);
		Integer result=coachSearchMapper.acceptMatch(map);
		if(result>0) {
			CoachDTO coach=coachMapper.getCoachInfo(user_idx);
	        // 2. 알림 내용 설정
	        map.put("notiType", "MATCH_ACCEPT");
	        map.put("content", coach.getNickname()+"님이 매칭을 수락했습니다.");

	        // 3. NOTIFICATION 테이블 삽입
	        coachSearchMapper.insertNotification(map);
		}
		return result;
	}

	@Override
	public boolean isLiked(int login_idx, int target_idx) throws Exception {
		HashMap<String, Object> map = new HashMap<>();
		map.put("login_idx", login_idx);
		map.put("target_idx", target_idx);
		Integer result=coachSearchMapper.isLiked(map);
		result= result==null?0:result;
		boolean like=result>0?true:false;
		return like;
	}

}