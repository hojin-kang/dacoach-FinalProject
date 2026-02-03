package com.dacoach.mapper.mypage;

import java.util.*;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MypageMapper {

	public Map<String, Object> getUserInfo(int users_idx);
}
