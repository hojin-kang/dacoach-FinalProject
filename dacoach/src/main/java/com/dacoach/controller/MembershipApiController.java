package com.dacoach.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.dacoach.kakaopay.KakaoApproveResponse;
import com.dacoach.kakaopay.KakaoCancelResponse;
import com.dacoach.kakaopay.KakaoPayService;
import com.dacoach.kakaopay.KakaoReadyResponse;
import com.dacoach.kakaopay.PayDTO;
import com.dacoach.kakaopay.PayStatusDTO;
import com.dacoach.mapper.kakaopay.KakaopayMapper;
import com.dacoach.mapper.membership.MembershipMapper;
import com.dacoach.model.membership.MembershipDTO;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/membershipApi")
@RequiredArgsConstructor
public class MembershipApiController {
	
	private String userPath;
	private KakaoReadyResponse kakaoReady;
	
	@Autowired
	private KakaopayMapper kakaopayMapper;
	private final KakaoPayService kakaoPayService;
	private HttpSession session;
	@Autowired
	private MembershipMapper membershipMapper;
	
	
		
	
	/**결제요청*/
	@PostMapping("/ready")
	public KakaoReadyResponse readyToKakaoPay(@RequestBody Map<String,Object> parameters,String path,HttpSession se) {
		 this.kakaoReady=kakaoPayService.kakaoPayReady(parameters);
		 userPath=path;
		 session=se;
		 return kakaoReady;
	}
	
	//결제성공
	@GetMapping("/success")
	public ModelAndView afterPayRequest(@RequestParam("pg_token") String pgToken){
		
		ModelAndView mav=new ModelAndView();
		KakaoApproveResponse kakaoApprove= kakaoPayService.approveResponse(pgToken);
		try {
			Map<String,String> map=new HashMap<String,String>();
			map.put("status", "완료됨");
			map.put("tid", kakaoApprove.getTid());
			kakaoApprove.setSid("담에추가");
			kakaoApprove.setItem_code("담에추가");
			//kakaopayMapper.upPayStatus(map);
			kakaopayMapper.insertPay(kakaoApprove);
			
			MembershipDTO dto=membershipMapper.userMembershipInfo((Integer)session.getAttribute("user_idx"));
			if(dto.getMember_detail_idx()==1) {
				dto.setMember_detail_idx(2);
				membershipMapper.membershipUpdate(dto);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		mav.addObject("msg","결제가 완료되었습니다");
		mav.addObject("url","/membership/membershipForm");
		mav.setViewName("alert");
		return mav;
		
	}
	
	/**결제 진행 취소*/
	@GetMapping("/cancel")
	public ModelAndView cancel() {
				
		try {
			Map<String,String> map=new HashMap<String,String>();
			map.put("status", "취소됨");
			map.put("tid", kakaoReady.getTid());
			kakaopayMapper.upPayStatus(map);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ModelAndView mav=new ModelAndView();
		mav.addObject("msg","결제가 취소되었습니다");
		mav.addObject("url",userPath);
		mav.setViewName("alert");
		return mav;
	}
	/**결제 실패*/
	@GetMapping("/fail")
	public ModelAndView fail() {
		try {
			Map<String,String> map=new HashMap<String,String>();
			map.put("status", "실패함");
			map.put("tid", kakaoReady.getTid());
			kakaopayMapper.upPayStatus(map);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ModelAndView mav=new ModelAndView();
		mav.addObject("msg","결제가 실패하였습니다");
		mav.addObject("url",userPath);
		mav.setViewName("alert");
		return mav;
	}
	
	/*결제 취소*/
	
	//결제취소
		@PostMapping("cancel")
		public ResponseEntity<String> cancelKakaoPay(@RequestBody Map<String,Object> cancelData) {
			//var parameter={cid:"yml에서 받기",tid:"DB에서뺴오기",cancel_amount:총가격,cancel_vat_amount:부과세,cancel_tax_free_amount:면세,payload:"취소사유"};
			
			String msg="결제 취소요청이 실패하였습니다 관리자에게 문의 부탁드립니다";
			
			try {
				System.out.println((int)cancelData.get("payIdx"));
				PayDTO payDto=kakaopayMapper.paySelect((int)cancelData.get("payIdx"));
				System.out.println(payDto);
				
				Map<String,Object> parameters=new HashMap<String,Object>();
				parameters.put("cid", payDto.getCid());
				parameters.put("tid", payDto.getTid() );
				parameters.put("cancel_amount",payDto.getTotal());
				parameters.put("cancel_vat_amount",payDto.getVat() );
				parameters.put("cancel_tax_free_amount",payDto.getTax_free() );
				parameters.put("payload",cancelData.get("payload"));
				
				KakaoCancelResponse kakaocancel=kakaoPayService.cancelResponse(parameters);
				if(kakaocancel!=null||kakaocancel.getStatus().equalsIgnoreCase("CANCEL_PAYMENT")) {
					int result=kakaopayMapper.cancelOk(kakaocancel);
					if(result>0) {
						Map<String,String> map=new HashMap<String,String>();
						map.put("status", "환불");
						map.put("tid", kakaoReady.getTid());
						int upResult=kakaopayMapper.upPayStatus(map);
						if(result>0&&upResult>0)msg="결제 취소가 성공적으로 진행됬습니다";
					}else msg="결제 취소 진행중 오류가 발생하였습니다";
													
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			return new ResponseEntity<String>(msg,HttpStatus.OK);
		}
}
