package com.dacoach.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.dacoach.model.coach.CoachDTO;
import com.dacoach.model.users.UsersDTO;
import com.dacoach.service.coach.CoachService;
import com.dacoach.service.kakao.KakaoService;
import com.dacoach.service.users.UsersService;

import jakarta.servlet.http.HttpSession;

@Controller
public class UserController {
	@Autowired
	private UsersService usersService;
	@Autowired
	private CoachService coachService;
	@Autowired
	private KakaoService kakaoService;

    // 1. 카카오 로그인 페이지로 리다이렉트
    @GetMapping("/auth/kakao")
    public String kakaoLogin() {
        String kakaoUrl = "https://kauth.kakao.com/oauth/authorize?"
                + "client_id=836bfad03ae2127905c6623948062759"
                + "&redirect_uri=http://localhost:9090/auth/kakao/callback"
                + "&response_type=code";
        return "redirect:" + kakaoUrl;
    }
    @GetMapping("/auth/kakao/callback")
    public ModelAndView kakaoCallback(@RequestParam String code, HttpSession session) {
        ModelAndView mav = new ModelAndView();
        
        try {
            // 1. 토큰 및 사용자 정보 가져오기
            String accessToken = kakaoService.getAccessToken(code);
            Map<String, Object> userInfo = kakaoService.getUserInfo(accessToken);
            
            // 정보가 없는 경우 예외 처리
            if (userInfo == null || userInfo.get("id") == null) {
                mav.setViewName("redirect:/login?error=kakao");
                return mav;
            }

            // 카카오 ID 값은 숫자형태이므로 안전하게 String.valueOf 사용을 권장합니다.
            String kakaoKey = String.valueOf(userInfo.get("id"));
            String nickname = (String) userInfo.get("nickname");

            // 2. 기존 회원 여부 확인
            CoachDTO coach = coachService.getCoachByKakaoKey(kakaoKey);

            if (coach != null) {
                // 이미 가입된 회원이면 세션 저장 후 메인으로
                session.setAttribute("user_idx", coach.getUser_idx());
                session.setAttribute("photo", coach.getPhoto());
                mav.setViewName("redirect:/");
            } else {
                // 신규 회원이면 가입 페이지로 이동 (데이터 포함)
                mav.addObject("kakao_key", kakaoKey);
                mav.addObject("nickname", nickname);
                mav.addObject("newKakaoUser", "가입된 정보가 없습니다. 접속하신 카카오 계정 정보로 회원가입 진행됩니다.");
                mav.setViewName("coach/coachJoin"); 
            }

        } catch (Exception e) {
            e.printStackTrace();
            mav.setViewName("redirect:/login");
        }
        
        return mav;
    }
	
	@GetMapping("/logout")
	public String logout(HttpSession session) {
		session.invalidate();
		return "redirect:/";
	}
	@GetMapping("/login")
	public String login() {
		return "users/login";
	}
	@GetMapping("/userType")
	public String userType() {
		return "users/userType";
	}
	@PostMapping("/loginOk")
	public ModelAndView loginOk(UsersDTO udto, HttpSession session) {
	    ModelAndView mav = new ModelAndView();

	    if (udto == null) {
	        mav.setViewName("users/login");
	        return mav;
	    }

	    try {
	        UsersDTO loginUser = usersService.userLogin(udto);

	        if (loginUser == null) {
	            mav.setViewName("users/login");
	            return mav;
	        }
	        
	        session.setAttribute("user_idx", loginUser.getUser_idx());
	        
	        CoachDTO coachInfo = coachService.getCoachInfo(loginUser.getUser_idx());
	        if (coachInfo != null && coachInfo.getPhoto() != null) {
	            session.setAttribute("photo", coachInfo.getPhoto());
	        } else {
	            session.setAttribute("photo", "default_profile.png");
	        }

	        mav.setViewName("redirect:/");

	    } catch (Exception e) {
	        e.printStackTrace();
	        mav.setViewName("users/login");
	    }
	    return mav;
	}
	
	
}
