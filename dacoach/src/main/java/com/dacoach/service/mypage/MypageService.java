package com.dacoach.service.mypage;

import java.util.*;

public interface MypageService {

	public Map getUserInfo(int user_idx);
	
	List<Map<String,Object>> getWithdrawReasons();
	void withdrawUser(int userIdx, int reasonTypeIdx, String extra);
}
