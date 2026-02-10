package com.dacoach.service.coach;

import java.util.*;

import org.springframework.web.multipart.MultipartFile;

import com.dacoach.model.coach.CoachDTO;
import com.dacoach.model.users.UsersDTO;

public interface CoachService {
	public boolean idCheck(String username) throws Exception;
	public Integer coachProfile(UsersDTO udto) throws Exception;
	public boolean checkNick(String nickname) throws Exception;
	public boolean emailCheck(String email) throws Exception;
	public boolean emailCompanyCheck(String email) throws Exception;
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
    public void saveCoachDetails(int user_idx, int myMinorCate, int interMinorCate, int myMajorRegion, int myMinorRegion,String myHashtags,String interHashtags) throws Exception;
    public Integer pwdChange(String login_id, String password) throws Exception;
    public Integer likeCoach(int login_idx, int targer_idx) throws Exception;
    public Integer unlikeCoach(int login_idx, int targer_idx) throws Exception;
    // 개인정보 수정
    public void updateMyInfo(
    	    CoachDTO dto,
    	    MultipartFile uploadPhoto,
    	    MultipartFile uploadVideo,
    	    int myMinorCate,
    	    int interMinorCate,
    	    int myMajorRegion,
    	    int myMinorRegion,
    	    String myHashtags,
    	    String interHashtags
    	) throws Exception;
    
    Map<String, Object> getMyProvideField(int user_idx) throws Exception;
    Map<String, Object> getMyInterestField(int user_idx) throws Exception;
    Map<String, Object> getMyRegion(int user_idx) throws Exception;

    List<String> getMyHashtags(int user_idx) throws Exception;
    List<String> getInterHashtags(int user_idx) throws Exception;
}
