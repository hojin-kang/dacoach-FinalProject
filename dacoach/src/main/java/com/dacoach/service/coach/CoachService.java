package com.dacoach.service.coach;

import java.util.*;

import org.springframework.web.multipart.MultipartFile;

import com.dacoach.model.coach.CoachDTO;
import com.dacoach.model.company.CertDTO;
import com.dacoach.model.dicip.DicipDTO;
import com.dacoach.model.minorField.MinorFieldDTO;
import com.dacoach.model.review.ReviewClassDTO;
import com.dacoach.model.schedule.ScheduleDTO;
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
    
    // 인기 코치 불러오기
    public List<CoachDTO> getPopularCoach() throws Exception;
    // 인기 분야 불러오기
    public List<MinorFieldDTO> getPopularField() throws Exception;

    public List<CertDTO> getCoachCertList(int user_idx) throws Exception;
    
    List<String> getCoachReviewTags() throws Exception;
    
    boolean writeCoachReview(ReviewClassDTO review) throws Exception;
    
    public DicipDTO getDicip(int user_idx, int target_idx) throws Exception;
    
    public int writeDicip(DicipDTO dto) throws Exception;
    
    public int agreeDicip(int agreement_idx) throws Exception;
    
    public int createSchedule(ScheduleDTO dto) throws Exception;
    
    public List<ScheduleDTO> getSchedule(int agreement_idx) throws Exception;
    
    public int deleteAgree(int user_idx, int target_idx) throws Exception;
    
    public UsersDTO getUserInfo(int user_idx) throws Exception;

}
