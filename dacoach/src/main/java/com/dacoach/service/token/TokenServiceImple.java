package com.dacoach.service.token;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dacoach.mapper.token.TokenMapper;
import com.dacoach.model.token.TokenHistoryDTO;

@Service
public class TokenServiceImple implements TokenService {

    @Autowired
    private TokenMapper tokenMapper;

    @Override
    public Integer getMyToken(Integer user_idx) throws Exception {
        return tokenMapper.getMyToken(user_idx);
    }

    @Override
    public java.util.List<TokenHistoryDTO> getTokenHistory(Integer user_idx) throws Exception {
        return tokenMapper.getTokenHistory(user_idx);
    }

    @Transactional
    @Override
    public void chargeTokenAfterPay(Integer user_idx, Integer qty) throws Exception {

        Integer cur = tokenMapper.getMyToken(user_idx);
        if (cur == null) cur = 0;

        int after = cur + qty;

        tokenMapper.updateTokenBalance(user_idx, after);

        Map<String, Object> map = new HashMap<>();
        map.put("user_idx", user_idx);
        map.put("hist_type", "충전");
        map.put("amount", qty);
        map.put("balance_after", after);

        tokenMapper.insertTokenHistory(map);
    }
    
    @Override
    public List<TokenHistoryDTO> getChargeHistory(Integer user_idx) throws Exception {
    	return tokenMapper.getChargeHistory(user_idx);
    }
}
