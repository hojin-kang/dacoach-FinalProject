package com.dacoach.service.coachSearch;

import java.util.*;

import com.dacoach.model.coach.CoachDTO;

public interface CoachSearchService {
	public List<CoachDTO> coachList(int cp, HashMap<String,Object> map) throws Exception;
	public List getCoachHashtags(int user_idx) throws Exception;
	public List getInterHashtags(int user_idx) throws Exception;
	public void applyMatchOrChat(int me, int target, String type) throws Exception;
	CoachDTO getCoachDetailStatus(int target_idx, int login_idx) throws Exception;
	public Integer useTokens(int user_idx, int amount) throws Exception;
	
	public Integer addTokenHistory(Integer user_idx, String hist_type, Integer amount, Integer balance_after) throws Exception;
}
