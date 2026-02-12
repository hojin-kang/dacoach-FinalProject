package com.dacoach.service.token;

import java.util.List;

import com.dacoach.kakaopay.KakaoApproveResponse;
import com.dacoach.kakaopay.PayDTO;
import com.dacoach.model.token.TokenHistoryDTO;

public interface TokenService {
	
    Integer getMyToken(Integer user_idx) throws Exception;
    List<TokenHistoryDTO> getTokenHistory(Integer user_idx) throws Exception;

    void chargeTokenAfterPay(Integer user_idx, Integer qty, KakaoApproveResponse approve) throws Exception;

    
    List<TokenHistoryDTO> getChargeHistory(Integer user_idx) throws Exception;
    
    List<PayDTO> getPayHistory(Integer user_idx) throws Exception;
}
