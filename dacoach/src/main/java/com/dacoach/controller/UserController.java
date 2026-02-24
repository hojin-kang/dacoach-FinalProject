package com.dacoach.controller;

import java.net.http.HttpRequest;
import java.sql.*;
import java.sql.Date;
import java.util.*;
import java.util.Calendar;
import java.util.HashMap;
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
import com.dacoach.model.company.CompanyDTO;
import com.dacoach.model.membership.MembershipDTO;
import com.dacoach.model.users.UsersDTO;
import com.dacoach.service.coach.CoachService;
import com.dacoach.service.company.CompanyService;
import com.dacoach.service.kakao.KakaoService;
import com.dacoach.service.membership.MembershipService;
import com.dacoach.service.users.UsersService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
public class UserController {
	@Autowired
	private UsersService usersService;
	@Autowired
	private CoachService coachService;
	@Autowired
	private KakaoService kakaoService;
	@Autowired
	private CompanyService companyService;
	@Autowired
	private MembershipService membershipService;

	// 1. 카카오 로그인 페이지로 리다이렉트
	@GetMapping("/auth/kakao")
	public String kakaoLogin(HttpServletRequest request) {
		String uri=request.getRequestURL().toString();
		String kakaoUrl = "https://kauth.kakao.com/oauth/authorize?" + "client_id=836bfad03ae2127905c6623948062759"
				+ "&redirect_uri="+uri+"/callback" + "&response_type=code";
		return "redirect:" + kakaoUrl;
	}

	@GetMapping("/auth/kakao/callback")
	public ModelAndView kakaoCallback(@RequestParam String code, HttpSession session, HttpServletRequest request) {
		ModelAndView mav = new ModelAndView();
		String uri=request.getRequestURL().toString();
		try {
			// 토큰 및 사용자 정보 가져오기
			String accessToken = kakaoService.getAccessToken(code, uri);
			// (id, 카카오 고유 번호(long타입)), (nickname, 닉네임(문자열)), (email, 이메일(문자열))
			Map<String, Object> userInfo = kakaoService.getUserInfo(accessToken);

			// 정보가 없는 경우 예외 처리
			if (userInfo == null || userInfo.get("id") == null) {
				mav.setViewName("redirect:/login?error=kakao");
				return mav;
			}

			// 카카오 ID 값은 숫자형태이므로 String.valueOf 사용
			String kakaoKey = String.valueOf(userInfo.get("id"));

			// 기존 회원 여부 확인
			CoachDTO coach = coachService.getCoachByKakaoKey(kakaoKey);

			if (coach != null && session.getAttribute("user_idx") == null) {
				// 이미 가입된 회원인 경우
				List<UsersDTO> suspendedLogs = usersService.getSuspendedLogs(coach.getUser_idx());
				//정지여부 확인
				if(suspendedLogs!=null&&suspendedLogs.size()>0) {
					StringBuilder reasons=new StringBuilder();
					for(UsersDTO log:suspendedLogs) {
						reasons.append("정지사유: "+log.getReason()+" / 기간: "+log.getStart_date()+" ~ "+log.getEnd_date()+"\n");
					}
					mav.addObject("msg", "현재 정지된 회원입니다.\n"+reasons.toString()+"로그인 불가합니다.");
					mav.addObject("url", "/login");
					mav.setViewName("alert");
					return mav;
				}
				//정지기간 지난경우
				List<UsersDTO> expiredLogs = usersService.getExpiredLogs(coach.getUser_idx());
				if(expiredLogs!=null&&expiredLogs.size()>0) {
					usersService.changeStatusToActive(coach.getUser_idx());
				}
				
				if (coach.getPhoto() != null) {
					session.setAttribute("photo", coach.getPhoto().equals("") ? null : coach.getPhoto());
				}
				
				
				session.setAttribute("user_idx", coach.getUser_idx());
				session.setAttribute("kakao", coach.getKakao_key());
				mav.addObject("msg", "카카오 연동 로그인 성공!\n메인페이지로 이동합니다.");
				mav.addObject("url", "/");
				mav.setViewName("alert");
			} else if (session.getAttribute("user_idx") != null && coach == null) {
				// 로그인 중이지만 카카오 연동이 안된 회원
				HashMap<String, Object> conKakao = new HashMap<>();
				conKakao.put("user_idx", (Integer) session.getAttribute("user_idx"));
				conKakao.put("kakao_key", kakaoKey);
				int result = coachService.connectKakao(conKakao);
				if (result > 0) {
					session.invalidate();
					mav.addObject("msg", "카카오 계정 연동 성공!\n다시 로그인해주세요.");
					mav.addObject("url", "/login");
					mav.setViewName("alert");
				} else {
					mav.addObject("msg", "카카오 계정 연동 실패!\n마이페이지로 이동합니다.");
					mav.addObject("url", "/mypage");
					mav.setViewName("alert");
				}
			} else {
				// 로그인 중도 아니었고 신규 회원이면 가입 페이지로 이동 (데이터 포함)
				session.setAttribute("kakao_key", kakaoKey);
				mav.addObject("msg", "가입된 정보가 없습니다. 접속하신 카카오 계정 정보로 회원가입 진행됩니다.");
				mav.addObject("url", "/coachJoin");
				mav.setViewName("alert");

			}

		} catch (Exception e) {
			e.printStackTrace();
			mav.addObject("msg", "카카오 계정 연동 로그인 실패!\n마이페이지로 이동합니다.");
			mav.addObject("url", "/mypage");
			mav.setViewName("alert");
		}

		return mav;
	}

	@GetMapping("/logout")
	public ModelAndView logout(HttpSession session) {
		ModelAndView mav = new ModelAndView();
		mav.addObject("msg", "로그아웃 되었습니다.\n오늘도 좋은 하루 되세요!");
		mav.addObject("url", "/");
		mav.setViewName("alert");
		session.invalidate();

		return mav;
	}

	@GetMapping("/login")
	public ModelAndView login(HttpSession session) {
		ModelAndView mav = new ModelAndView();
		if (session.getAttribute("user_idx") != null) {
			mav.addObject("msg", "이미 로그인 중입니다.");
			mav.addObject("url", "/");
			mav.setViewName("alert");
		}else {
			mav.setViewName("users/login");
		}
		return mav;
	}
	
	@GetMapping("/needLogin")
	public ModelAndView needLogin(HttpSession session) {
		session.invalidate();
		ModelAndView mav = new ModelAndView();
		mav.addObject("msg", "로그인이 필요합니다.");
		mav.addObject("url", "/");
		mav.setViewName("alert");
		return mav;
	}

	@GetMapping("/userType")
	public String userType() {
		return "users/userType";
	}

	@PostMapping("/loginOk")
	public ModelAndView loginOk(UsersDTO udto, HttpSession session,String user_type) {
		ModelAndView mav = new ModelAndView();
		
		if (udto == null) {
			mav.addObject("msg", "ID 및 비밀번호를 확인해주세요");
			mav.addObject("url", "/login");
			mav.setViewName("alert");
			return mav;
		}
		

		
		if(user_type.equalsIgnoreCase("coach")) {
		try {
			
			UsersDTO loginUser = usersService.userLogin(udto);

			if (loginUser == null) {
				mav.addObject("msg", "ID 및 비밀번호를 확인해주세요");
				mav.addObject("url", "/login");
				mav.setViewName("alert");
				return mav;
			}
			//정지여부 확인
			try {
				List<UsersDTO> suspendedLogs = usersService.getSuspendedLogs(loginUser.getUser_idx());
				if(suspendedLogs!=null&&suspendedLogs.size()>0) {
					StringBuilder reasons=new StringBuilder();
					for(UsersDTO log:suspendedLogs) {
						reasons.append("정지사유: "+log.getReason()+" / 기간: "+log.getStart_date()+" ~ "+log.getEnd_date()+"\n");
					}
					mav.addObject("msg", "현재 정지된 회원입니다.\n"+reasons.toString()+"로그인 불가합니다.");
					mav.addObject("url", "/login");
					mav.setViewName("alert");
					return mav;
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//정지기간 지난 경우
			List<UsersDTO> expiredLogs = usersService.getExpiredLogs(loginUser.getUser_idx());
			if(expiredLogs!=null&&expiredLogs.size()>0) {
				usersService.changeStatusToActive(loginUser.getUser_idx());
			}
			if(loginUser.getStatus().equals("INACTIVE")) {
				usersService.deleteUser(loginUser.getUser_idx());
				mav.addObject("msg", "회원가입 절차가 비정상적으로 종료되었습니다. 회원가입을 다시 진행해주세요.");
				mav.addObject("url", "/coachJoin");
				mav.setViewName("alert");
				return mav;
			}
			
			session.setAttribute("user_idx", loginUser.getUser_idx());

			CoachDTO coachInfo = coachService.getCoachInfo(loginUser.getUser_idx());
			if (coachInfo != null) {
				if (coachInfo.getPhoto() != null) {
					session.setAttribute("photo", coachInfo.getPhoto().equals("") ? null : coachInfo.getPhoto());
				}
				if (coachInfo.getKakao_key() != null) {
					session.setAttribute("kakao",
							coachInfo.getKakao_key().equals("") ? null : coachInfo.getKakao_key());
				}
			}
			mav.addObject("msg", "로그인 성공!\n메인페이지로 이동합니다");
			mav.addObject("url", "/");
			mav.setViewName("alert");

		} catch (Exception e) {
			e.printStackTrace();
			mav.addObject("msg", "ID 및 비밀번호를 확인해주세요");
			mav.addObject("url", "/login");
			mav.setViewName("alert");
		}
		//기업 로그인 부분임당
		}else if(user_type.equalsIgnoreCase("company")) {
			
			try {
				UsersDTO loginUser = usersService.userLogin(udto);
				
				if (loginUser == null) {
					mav.addObject("msg", "ID 및 비밀번호를 확인해주세요");
					mav.addObject("url", "/login");
					mav.setViewName("alert");
					return mav;
				}
				CompanyDTO dto=companyService.getCompanyInfo(loginUser.getUser_idx());
				if(dto==null) {
					mav.addObject("msg","회원가입이 완료되지 않았습니다 계속 진행하겠습니다");
					mav.addObject("url","/company/profile/companyInfo?login_id="+loginUser.getLogin_id());
					mav.setViewName("alert");
					return mav;
				}else {
					if(!companyService.regionCheck(dto.getCompany_idx())||!companyService.provideCheck(dto.getCompany_idx())) {			
						mav.addObject("msg","회원가입이 완료되지 않았습니다 계속 진행하겠습니다");
						mav.addObject("url","/company/profile/profileForm?userIdx="+loginUser.getUser_idx());
						mav.setViewName("alert");
						return mav;
					}
				}
				//정지여부 확인
				try {
					List<UsersDTO> suspendedLogs = usersService.getSuspendedLogs(udto.getUser_idx());
					if(suspendedLogs!=null&&suspendedLogs.size()>0) {
						StringBuilder reasons=new StringBuilder();
						for(UsersDTO log:suspendedLogs) {
							reasons.append("정지사유: "+log.getReason()+" / 기간: "+log.getStart_date()+" ~ "+log.getEnd_date()+"\n");
						}
						mav.addObject("msg", "현재 정지된 회원입니다.\n"+reasons.toString()+"로그인 불가합니다.");
						mav.addObject("url", "/login");
						mav.setViewName("alert");
						return mav;
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				//정지기간 지난경우
				List<UsersDTO> expiredLogs = usersService.getExpiredLogs(loginUser.getUser_idx());
				if(expiredLogs!=null&&expiredLogs.size()>0) {
					usersService.changeStatusToActive(loginUser.getUser_idx());
				}
				
				if(loginUser.getStatus().equals("INACTIVE")) {
				
					mav.addObject("msg", "아직 가입이 수락되지 않았습니다");
					mav.addObject("url", "/login");
					mav.setViewName("alert");
					return mav;
				
				}
								
				session.setAttribute("user_idx", loginUser.getUser_idx());
				session.setAttribute("user_type", loginUser.getUser_type());
				session.setAttribute("user_name", loginUser.getUser_name());
				session.setAttribute("company_idx", dto.getCompany_idx());
				if(dto.getPhoto() != null ) {
					session.setAttribute("photo", dto.getPhoto().equals("")? null : dto.getPhoto());
				}
				/**
				MembershipDTO membershipDto=membershipService.userMembershipInfo(loginUser.getUser_idx());
				Calendar now=Calendar.getInstance();
				int year=now.get(Calendar.YEAR);
				int month=now.get(Calendar.MONTH)+1;
				int day=now.get(Calendar.DATE);
				String strDate=""+year+"-"+month+"-"+day;
				Date nowDay=Date.valueOf(strDate);
				
				if(membershipDto.getEnd_date()!=null
						&&membershipDto.getEnd_date().before(nowDay)&&membershipDto.getStatus().equals("취소")) {
					membershipService.autoUpdate(membershipDto);
					
				}
				*/
				mav.addObject("msg", "로그인 성공!\n메인페이지로 이동합니다");
				mav.addObject("url", "/");
				mav.setViewName("alert");
				
				
			} catch (Exception e) {
				
				e.printStackTrace();
				
				mav.addObject("msg", "ID 및 비밀번호를 확인해주세요");
				mav.addObject("url", "/login");
				mav.setViewName("alert");
			}
			
		}
		return mav;
	
		
	}
	//id 찾기 페이지 이동
	@GetMapping("/idFind")
	public ModelAndView idFindForm(HttpSession session) {
		ModelAndView mav=new ModelAndView();
		if(session.getAttribute("user_idx")!=null) {
			mav.addObject("msg", "이미 로그인 중입니다.");
			mav.addObject("url", "/");
			mav.setViewName("alert");
		}else {
			mav.setViewName("users/idFind");
		}
		
		return mav;
	}
	//비밀번호 찾기 페이지 이동
	@GetMapping("/pwdFind")
	public ModelAndView pwFindForm(HttpSession session) {
		ModelAndView mav=new ModelAndView();
		if(session.getAttribute("user_idx")!=null) {
			mav.addObject("msg", "이미 로그인 중입니다.");
			mav.addObject("url", "/");
			mav.setViewName("alert");
		}else {
			mav.setViewName("users/pwdFind");
		}
		
		return mav;
	}
	//비밀번호 변경
	@PostMapping("/pwdChange")
	public ModelAndView pwdChange(@RequestParam(value="login_id", required = true)String login_id,
			@RequestParam(value="password", required = true)String password,
			HttpSession session) {
		ModelAndView mav=new ModelAndView();
		try {
			int result=coachService.pwdChange(login_id,password);
			if(result>0) {
				mav.addObject("msg", "비밀번호 변경이 완료되었습니다.");
				mav.addObject("url", "/login");
				mav.setViewName("alert");
			}else {
				mav.addObject("msg", "비밀번호 변경에 실패했습니다. 다시 시도해주세요.");
				mav.addObject("url", "/pwdFind");
				mav.setViewName("alert");
			}
		} catch (Exception e) {
			e.printStackTrace();
			mav.addObject("msg", "비밀번호 변경에 실패했습니다. 다시 시도해주세요.");
			mav.addObject("url", "/pwdFind");
			mav.setViewName("alert");
		}
		return mav;
	}
	

}
