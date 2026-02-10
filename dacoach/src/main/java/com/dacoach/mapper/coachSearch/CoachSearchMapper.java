package com.dacoach.mapper.coachSearch;

import java.util.*;

import com.dacoach.model.coach.CoachDTO;

public interface CoachSearchMapper {
	public List<CoachDTO> coachList(HashMap map);
	public List getCoachHashtags(int user_idx);
	public List getInterHashtags(int user_idx);
    public int upsertMatchRequest(Map<String, Object> map);
    public int insertNotification(Map<String, Object> map);
    public CoachDTO getCoachDetailStatus(Map<String, Object> params);
    public Integer useTokens(Map<String, Object> map);
    public Integer addTokenHistory(Map thmap);
    public Integer acceptChat(Map<String, Object> map);
    public Integer acceptMatch(Map<String, Object> map);
    public Integer isLiked(Map<String, Object> map);
}
