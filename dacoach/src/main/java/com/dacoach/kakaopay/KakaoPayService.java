package com.dacoach.kakaopay;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class KakaoPayService {
	
	private final KakaoPayProperties payProperties;
	private RestTemplate restTemplate = new RestTemplate();
	private KakaoReadyResponse kakaoReady;
	
	private HttpHeaders getHeaders() {
		HttpHeaders headers = new HttpHeaders();
		String auth="SECRET_KEY " + payProperties.getSecretKey();
		headers.set("Authorization", auth);
		headers.set("Content-Type","application/json");
		return headers;
	}
	
	
	  //결제 완료요청(값 수정해서 쓰면될듯)
	public KakaoReadyResponse kakaoPayReady(Map<String,Object> parameters) {	
		
		//외부에 보낼 url*/
		HttpEntity<Map<String,Object>> requestEntity= new HttpEntity<>(parameters,getHeaders());
		RestTemplate restTemplate=new RestTemplate();
		
		kakaoReady = restTemplate.postForObject(
				"https://open-api.kakaopay.com/online/v1/payment/ready", 
				requestEntity,
				KakaoReadyResponse.class);
		
		return kakaoReady;
	}
	//결제 완료 승인(결제화면 나오는 메서드 ex-QR결제)
	public KakaoApproveResponse approveResponse(String pgToken,String test) {
		//카카오 요청
		Map<String,String> parameters=new HashMap<>();
		parameters.put("cid", payProperties.getCid());
		parameters.put("tid", kakaoReady.getTid());
		parameters.put("partner_order_id",test);
		parameters.put("partner_user_id","2");
		parameters.put("pg_token",pgToken);
		
		//파라미터, 헤더
		HttpEntity<Map<String,String>> requestEntity=new HttpEntity<>(parameters,this.getHeaders());
		System.out.println();
		System.out.println();
		System.out.println(requestEntity);
		System.out.println();
		System.out.println();
		
		//외부에 보낼 url
		RestTemplate restTemplate=new RestTemplate();
		
		KakaoApproveResponse approveResponse = 
				restTemplate.postForObject(
						"https://open-api.kakaopay.com/online/v1/payment/approve", 
						requestEntity, 
						KakaoApproveResponse.class);
		
		System.out.println();
		System.out.println();
		System.out.println();
		System.out.println(approveResponse);
		System.out.println();
		System.out.println();
		System.out.println();
		return approveResponse;
	}
}
