package com.dacoach.service.coachSearch;

import java.util.*;

import com.dacoach.model.coach.CoachDTO;
import com.dacoach.model.review.ReviewCoachDTO;

public interface CoachSearchService {
	public List<CoachDTO> coachList(int cp, HashMap<String,Object> map) throws Exception;
	public List getCoachHashtags(int user_idx) throws Exception;
	public List getInterHashtags(int user_idx) throws Exception;
	public void applyMatchOrChat(int me, int target, String type) throws Exception;
	CoachDTO getCoachDetailStatus(int target_idx, int login_idx) throws Exception;
	public Integer useTokens(int user_idx, int amount) throws Exception;
	public Integer addTokenHistory(Integer user_idx, String hist_type, Integer amount, Integer balance_after) throws Exception;
	public Integer acceptChat(int user_idx, int target_idx) throws Exception;
	public Integer acceptMatch(int user_idx, int target_idx) throws Exception;
	public boolean isLiked(int login_idx, int target_idx) throws Exception;
	
	
}
