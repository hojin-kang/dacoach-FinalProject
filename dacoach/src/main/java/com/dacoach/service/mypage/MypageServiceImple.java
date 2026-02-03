package com.dacoach.service.mypage;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}
