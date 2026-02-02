package com.dacoach.mapper.coach;

import java.util.*;

import com.dacoach.model.coach.CoachDTO;
import com.dacoach.model.users.UsersDTO;

public interface CoachMapper {
	public Integer idCheck(String username) throws Exception;
	public Integer coachProfile(UsersDTO udto) throws Exception;
	public boolean checkNick(String nickname) throws Exception;
	public List<Map<String, Object>> getMajorFields() throws Exception;
	public List<Map<String, Object>> getMinorFields(int majorIdx) throws Exception;
	List<Map<String, Object>> getMajorRegions() throws Exception;
    List<Map<String, Object>> getMinorRegions(int majorRegionIdx) throws Exception;
    public Integer getUsersIdx(String login_id) throws Exception;
    public Integer coachJoin(CoachDTO cdto) throws Exception;
}
