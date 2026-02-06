package com.dacoach.model.company;

	import java.util.Map;

	import org.apache.catalina.connector.Response;
	import org.springframework.http.HttpHeaders;
	import org.springframework.http.HttpStatus;
	import org.springframework.http.ResponseEntity;
	import org.springframework.web.bind.annotation.GetMapping;
	import org.springframework.web.bind.annotation.PostMapping;
	import org.springframework.web.bind.annotation.RequestBody;
	import org.springframework.web.bind.annotation.RequestHeader;
	import org.springframework.web.bind.annotation.RequestMapping;
	import org.springframework.web.bind.annotation.RequestParam;
	import org.springframework.web.bind.annotation.RestController;
	import org.springframework.web.servlet.ModelAndView;

import com.dacoach.kakaopay.KakaoApproveResponse;
import com.dacoach.kakaopay.KakaoPayService;
import com.dacoach.kakaopay.KakaoReadyResponse;

import lombok.RequiredArgsConstructor;

	@RestController
	@RequestMapping("/payment")
	@RequiredArgsConstructor
	public class KakaopaySample {
		
		private final KakaoPayService kakaoPayService;
		
		/**결제요청*/
		@PostMapping("/ready")
		public KakaoReadyResponse readyToKakaoPay(@RequestBody Map<String,Object> parameters) {
			return kakaoPayService.kakaoPayReady(parameters);
		}
		
		//결제성공
		@GetMapping("/success")
		public ResponseEntity<KakaoApproveResponse> afterPayRequest(@RequestParam("pg_token") String pgToken,String test){
			
			KakaoApproveResponse kakaoApprove= kakaoPayService.approveResponse(pgToken,test);
			
			return new ResponseEntity<KakaoApproveResponse>(kakaoApprove,HttpStatus.OK);
			
		}
		/**결제 진행 취소*/
//		@GetMapping("/cancel")
//		public void cancel() {
//			throw new BusinessLogicException(ExceptionCode.PAY_CANCEL);
//		}
		/**결제 실패*/
//		@GetMapping("/fail")
//		public void fail() {
//			throw new BusinessLogicException(ExceptionCode.PAY_FAILED);
//		}
}
