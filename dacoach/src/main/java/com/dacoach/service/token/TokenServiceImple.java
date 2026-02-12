package com.dacoach.service.token;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dacoach.kakaopay.KakaoApproveResponse;
import com.dacoach.kakaopay.PayDTO;
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
    public void chargeTokenAfterPay(Integer user_idx, Integer qty, KakaoApproveResponse approve) throws Exception {

        // 0) PAY 먼저 insert
        PayDTO pay = new PayDTO();
        pay.setTid(approve.getTid());
        pay.setCid(approve.getCid()); // 없으면 null이어도 됨
        pay.setSid(approve.getSid()); // 없으면 null이어도 됨
        pay.setPartner_order_id(approve.getPartner_order_id());
        pay.setPartner_user_id(approve.getPartner_user_id());
        pay.setItem_name(approve.getItem_name());
        pay.setItem_code(approve.getItem_code());
        pay.setQuantity(approve.getQuantity());
        pay.setCreated_at(approve.getCreated_at());
        pay.setApproved_at(approve.getApproved_at());

        if (approve.getAmount() != null) {
            pay.setTotal(approve.getAmount().getTotal());
            pay.setTax_free(approve.getAmount().getTax_free());
            pay.setTax(approve.getAmount().getTax());
            pay.setPoint(approve.getAmount().getPoint());
            pay.setDiscount(approve.getAmount().getDiscount());
            pay.setGreen_deposit(approve.getAmount().getGreen_deposit());
        }

        pay.setPay_type("TOKEN");

        int payResult = tokenMapper.insertPay(pay);
        if (payResult <= 0) throw new RuntimeException("PAY insert failed");

        // 1) 토큰 충전(기존 로직)
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
    
    @Override
    public List<PayDTO> getPayHistory(Integer user_idx) throws Exception {
        return tokenMapper.getPayHistory(String.valueOf(user_idx));
    }

	@Override
	public int giftToken(int user_idx, int qty) throws Exception {
		
        // 1) 토큰 충전(기존 로직)
        Integer cur = tokenMapper.getMyToken(user_idx);
        if (cur == null) cur = 0;

        int after = cur + qty;
        int result = 0;
        result+=tokenMapper.updateTokenBalance(user_idx, after);

        Map<String, Object> map = new HashMap<>();
        map.put("user_idx", user_idx);
        map.put("hist_type", "선물");
        map.put("amount", qty);
        map.put("balance_after", after);

        result+=tokenMapper.insertTokenHistory(map);
        
		return result;
	}
}
