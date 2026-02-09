package com.dacoach.service.token;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dacoach.mapper.token.TokenMapper;
import com.dacoach.model.token.TokenHistoryDTO;
import java.util.*;

@Service
public class TokenServiceImple implements TokenService {

	@Autowired
	private TokenMapper tokenMapper;
	
	@Override
	public Integer getMyToken(Integer user_idx) throws Exception {
		return tokenMapper.getMyToken(user_idx);
	}
	
	@Override
	public List<TokenHistoryDTO> getTokenHistory(Integer user_idx) throws Exception {
		return tokenMapper.getTokenHistory(user_idx);
	}
}
