package com.dacoach.mapper.coachSearch;

import java.util.*;

import com.dacoach.model.coach.CoachDTO;

public interface CoachSearchMapper {
	public List<CoachDTO> coachList(HashMap map);
	public List getCoachHashtags(int user_idx);
}
