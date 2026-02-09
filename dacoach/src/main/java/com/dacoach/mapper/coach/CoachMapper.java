package com.dacoach.mapper.coach;

import java.util.*;

import org.apache.ibatis.annotations.Param;

import com.dacoach.model.coach.CoachDTO;
import com.dacoach.model.users.UsersDTO;

public interface CoachMapper {
	public Integer idCheck(String username) throws Exception;

	public Integer coachProfile(UsersDTO udto) throws Exception;

	public boolean checkNick(String nickname) throws Exception;

	public Integer emailCheck(String email) throws Exception;

	public List<Map<String, Object>> getMajorFields() throws Exception;

	public List<Map<String, Object>> getMinorFields(int majorIdx) throws Exception;

	public List<Map<String, Object>> getMajorRegions() throws Exception;

	public List<Map<String, Object>> getMinorRegions(int majorRegionIdx) throws Exception;

	public Integer getUsersIdx(String login_id) throws Exception;

	public Integer coachJoin(CoachDTO cdto) throws Exception;

	public CoachDTO getCoachInfo(int users_idx) throws Exception;

	public CoachDTO getCoachByKakaoKey(String kakaoKey) throws Exception;

	public int connectKakao(HashMap<String, Object> conKakao) throws Exception;

	public Integer activateCoach(int user_idx) throws Exception;
	
	public Integer saveMyMinorCate(HashMap map) throws Exception;
	
	public Integer saveInterMinorCate(HashMap map) throws Exception;
	
	public Integer saveMyRegions(HashMap map) throws Exception;
	
	public List<String> allHashtags() throws Exception;
	
	public Integer saveHashtags(HashSet set) throws Exception;
	
	public List<Integer> getHashtagIdxs(HashSet set) throws Exception;
	
	public void myHashtagMapping(@Param("user_idx")int user_idx,@Param("list")List<Integer> list) throws Exception;
	
	public void interHashtagMapping(@Param("user_idx")int user_idx,@Param("list")List<Integer> list) throws Exception;
}
