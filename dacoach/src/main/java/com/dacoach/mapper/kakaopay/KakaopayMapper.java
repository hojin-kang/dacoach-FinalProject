package com.dacoach.mapper.kakaopay;

import java.util.*;

import com.dacoach.kakaopay.KakaoApproveResponse;
import com.dacoach.kakaopay.KakaoCancelResponse;
import com.dacoach.kakaopay.PayDTO;
import com.dacoach.kakaopay.PayStatusDTO;
public interface KakaopayMapper {

	public int insertPayStatus(PayStatusDTO dto) throws Exception;
	
	public PayStatusDTO getPayStatus(String tid) throws Exception;
	
	public int upPayStatus(Map<String,String> map) throws Exception;
	
	public int insertPay(KakaoApproveResponse kar) throws Exception;
	
	public PayDTO paySelect(int pay_idx) throws Exception; 
	
	public int cancelOk(KakaoCancelResponse kcr) throws Exception;
	
	public List<Map<String,Object>> selectRefundList(int startRow,int endRow) throws Exception;
	
	PayDTO selectPayStatus(int pay_idx) throws Exception;
	
	int getRefundTotalCnt();
	
}
