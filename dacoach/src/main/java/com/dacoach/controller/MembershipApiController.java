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
import com.dacoach.kakaopay.KakaoPayService;
import com.dacoach.kakaopay.KakaoReadyResponse;
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
			kakaopayMapper.upPayStatus(map);
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
		System.out.println(userPath);
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
}
