package com.dacoach.mapper.coach;

import java.util.*;

import org.apache.ibatis.annotations.Param;

import com.dacoach.model.coach.CoachDTO;
import com.dacoach.model.company.CertDTO;
import com.dacoach.model.users.UsersDTO;

public interface CoachMapper {
	public Integer idCheck(String username) throws Exception;

	public Integer coachProfile(UsersDTO udto) throws Exception;

	public Integer checkNick(String nickname) throws Exception;

	public Integer emailCheck(String email) throws Exception;
	
	public Integer emailCompanyCheck(String email) throws Exception;

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
	
	public String getId(String email) throws Exception;
	
	public String getCompanyId(String email) throws Exception;
	
	public Integer pwdChange(HashMap map) throws Exception;
	
	public Integer likeCoach(HashMap map) throws Exception;
	
	public Integer unlikeCoach(HashMap map) throws Exception;
	
	public List<CertDTO> getCoachCertList(int user_idx) throws Exception;
	
	// 개인정보 수정
    int updateCoachInfo(CoachDTO dto) throws Exception;

    // 기존 선택값 로딩
    Map<String, Object> getMyProvideField(int user_idx) throws Exception;
    Map<String, Object> getMyInterestField(int user_idx) throws Exception;
    Map<String, Object> getMyRegion(int user_idx) throws Exception;

    // 기존 해시태그 로딩
    List<String> getMyHashtags(int user_idx) throws Exception;
    List<String> getInterHashtags(int user_idx) throws Exception;

    // 매핑 삭제
    int deleteProvideByCoachIdx(int coach_idx) throws Exception;
    int deleteInterestByCoachIdx(int coach_idx) throws Exception;
    int deleteRegionByCoachIdx(int coach_idx) throws Exception;

    int deleteMyHashtagMapping(int user_idx) throws Exception;
    int deleteInterHashtagMapping(int user_idx) throws Exception;
}
