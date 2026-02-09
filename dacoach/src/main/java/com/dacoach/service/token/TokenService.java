package com.dacoach.service.token;

import com.dacoach.model.token.TokenHistoryDTO;
import java.util.*;

public interface TokenService {

	public Integer getMyToken(Integer user_idx) throws Exception;
	
	public List<TokenHistoryDTO> getTokenHistory(Integer user_idx) throws Exception;
}
