package com.dacoach.service.coachSearch;

import java.util.*;

import com.dacoach.model.coach.CoachDTO;

public interface CoachSearchService {
	public List<CoachDTO> coachList(int cp, HashMap<String,Object> map) throws Exception;
	public List getCoachHashtags(int user_idx) throws Exception;
}
