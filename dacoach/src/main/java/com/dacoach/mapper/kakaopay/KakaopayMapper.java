package com.dacoach.mapper.kakaopay;

import java.util.*;

import com.dacoach.kakaopay.KakaoApproveResponse;
import com.dacoach.kakaopay.PayStatusDTO;
public interface KakaopayMapper {

	public int insertPayStatus(PayStatusDTO dto) throws Exception;
	
	public PayStatusDTO getPayStatus(String tid) throws Exception;
	
	public int upPayStatus(Map<String,String> map) throws Exception;
	
	public int insertPay(KakaoApproveResponse kar) throws Exception;
}
