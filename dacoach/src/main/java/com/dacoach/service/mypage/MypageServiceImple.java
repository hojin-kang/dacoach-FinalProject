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
	public Map getUserInfo(int users_idx) {
		Map userInfo = mypageMapper.getUserInfo(users_idx);
		return userInfo;
	}
}
