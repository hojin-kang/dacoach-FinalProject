package com.dacoach.service.mypage;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dacoach.mapper.mypage.MypageMapper;

@Service
public class MypageServiceImple implements MypageService {

	@Autowired
    private MypageMapper mypageMapper;
	
	@Override
	public Map getUserInfo(int user_idx) {
		Map userInfo = mypageMapper.getUserInfo(user_idx);
		return userInfo;
	}
	
	 // 탈퇴 사유 목록 조회
    @Override
    public List<Map<String, Object>> getWithdrawReasons() {
        return mypageMapper.getWithdrawReasons();
    }
	
	@Transactional
	@Override
	public void withdrawUser(int userIdx, int reasonTypeIdx) {

	    // 1) 유저 타입 조회
	    String userType = mypageMapper.getUserType(userIdx); // COACH / COMPANY / ADMIN

	    // 2) 탈퇴 사유 로그
	    Map<String,Object> log = new HashMap<>();
	    log.put("user_type", userType);
	    log.put("reason_type_idx", reasonTypeIdx);
	    mypageMapper.insertReasonLog(log);

	    // 3) 유저 삭제
	    mypageMapper.deleteUser(userIdx);

	}
}
