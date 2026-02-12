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
	public void withdrawUser(int userIdx, int reasonTypeIdx, String extra) {

	    // 1) 유저 타입 조회
	    String userType = mypageMapper.getUserType(userIdx); // COACH / COMPANY / ADMIN

	    // 2) 탈퇴 사유 로그
	    Map<String,Object> log = new HashMap<>();
	    log.put("user_idx", userIdx);
	    log.put("reason_type_idx", reasonTypeIdx);
	    log.put("extra", extra);
	    mypageMapper.insertReasonLog(log);

	    // 3) USERS 마스킹 + STATUS 변경
	    Map<String,Object> u = new HashMap<>();
	    u.put("user_idx", userIdx);
	    u.put("login_id", "deleted_" + userIdx + "@dacoach.local");
	    u.put("user_name", "탈퇴회원");
	    u.put("password", "DELETED"); // 너가 쓰는 해시 방식 있으면 그걸로
	    mypageMapper.maskUsers(u);

	    // 4) 상세테이블 마스킹
	    if ("COACH".equals(userType)) {
	        Map<String,Object> c = new HashMap<>();
	        c.put("user_idx", userIdx);
	        c.put("nickname", "탈퇴코치_" + userIdx);
	        c.put("mail", "deleted_" + userIdx + "@dacoach.local");
	        mypageMapper.maskCoach(c);
	    } else if ("COMPANY".equals(userType)) {
	        Map<String,Object> c = new HashMap<>();
	        c.put("user_idx", userIdx);
	        c.put("phone", "000-0000-" + userIdx);
	        c.put("address", "탈퇴");
	        mypageMapper.maskCompany(c);
	    }
	}
}
