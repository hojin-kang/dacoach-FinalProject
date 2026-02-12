package com.dacoach.mapper.mypage;

import java.util.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface MypageMapper {

	    Map<String, Object> getUserInfo(@Param("user_idx") int userIdx);
	    
	    List<Map<String,Object>> getWithdrawReasons();
	    String getUserType(@Param("user_idx") int userIdx);

	    int insertReasonLog(Map<String,Object> map);

	    int maskUsers(Map<String,Object> map);
	    int maskCoach(Map<String,Object> map);
	    int maskCompany(Map<String,Object> map);

}
