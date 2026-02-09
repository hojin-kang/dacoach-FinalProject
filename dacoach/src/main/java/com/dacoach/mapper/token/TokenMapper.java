package com.dacoach.mapper.token;

import org.apache.ibatis.annotations.Mapper;

import java.util.*;
import com.dacoach.model.token.TokenHistoryDTO;

@Mapper
public interface TokenMapper {

	public Integer getMyToken(Integer user_idx) throws Exception;
	
	public List<TokenHistoryDTO> getTokenHistory(Integer user_idx) throws Exception;
}
