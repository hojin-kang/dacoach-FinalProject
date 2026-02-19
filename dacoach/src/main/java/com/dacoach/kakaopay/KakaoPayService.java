package com.dacoach.kakaopay;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import com.dacoach.mapper.kakaopay.KakaopayMapper;

import jakarta.servlet.http.HttpSession;
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

	@Autowired
	private KakaopayMapper kakaopayMapper;

	private HttpHeaders getHeaders() {
		HttpHeaders headers = new HttpHeaders();
		String auth = "SECRET_KEY " + payProperties.getSecretKey();
		headers.set("Authorization", auth);
		headers.set("Content-Type", "application/json");
		return headers;
	}

	// 결제 완료요청(값 수정해서 쓰면될듯)
	public KakaoReadyResponse kakaoPayReady(Map<String, Object> parameters) {

		// 외부에 보낼 url*/
		HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(parameters, getHeaders());
		RestTemplate restTemplate = new RestTemplate();

		kakaoReady = restTemplate.postForObject("https://open-api.kakaopay.com/online/v1/payment/ready", requestEntity,
				KakaoReadyResponse.class);

		PayStatusDTO dto = new PayStatusDTO();
		dto.setTid(kakaoReady.getTid());
		dto.setPartner_order_id(String.valueOf(parameters.get("partner_order_id")));
		dto.setPartner_user_id(String.valueOf(parameters.get("partner_user_id")));
		dto.setStatus("완료");

		try {
			kakaopayMapper.insertPayStatus(dto);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return kakaoReady;
	}

	// 결제 완료 승인(결제화면 나오는 메서드 ex-QR결제)
	public KakaoApproveResponse approveResponse(String pgToken) {
		// 카카오 요청
		Map<String, String> parameters = new HashMap<>();
		try {
			PayStatusDTO dto = kakaopayMapper.getPayStatus(kakaoReady.getTid());
			parameters.put("partner_order_id", dto.getPartner_order_id());
			parameters.put("partner_user_id", dto.getPartner_user_id());// 이거 나중에 로그인 인가정보로 바꿀예정
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		parameters.put("cid", payProperties.getCid());
		parameters.put("tid", kakaoReady.getTid());
		parameters.put("pg_token", pgToken);

		// 파라미터, 헤더
		HttpEntity<Map<String, String>> requestEntity = new HttpEntity<>(parameters, this.getHeaders());
//		System.out.println();
//		System.out.println();
//		System.out.println(requestEntity);
//		System.out.println();
//		System.out.println();

		// 외부에 보낼 url
		RestTemplate restTemplate = new RestTemplate();

		KakaoApproveResponse approveResponse = restTemplate.postForObject(
				"https://open-api.kakaopay.com/online/v1/payment/approve", requestEntity, KakaoApproveResponse.class);

//		System.out.println();
//		System.out.println();
//		System.out.println();
//		System.out.println(approveResponse);
//		System.out.println();
//		System.out.println();
//		System.out.println();
		return approveResponse;
	}

	// 취소
	public KakaoCancelResponse cancelResponse(Map<String, Object> parameters) {

		HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(parameters, getHeaders());
		RestTemplate restTemplate = new RestTemplate();

		KakaoCancelResponse kakaocancel = restTemplate.postForObject(
				"https://open-api.kakaopay.com/online/v1/payment/cancel", requestEntity, KakaoCancelResponse.class);

		return kakaocancel;
	}
}
