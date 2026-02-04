package com.dacoach.controller;

import java.io.File;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.dacoach.model.coach.CoachDTO;
import com.dacoach.model.users.UsersDTO;
import com.dacoach.service.coach.CoachService;

import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
public class CoachController {
	@Autowired
	private CoachService coachService;
	
	
	@GetMapping("/coachJoin")
	public String coachJoinForm() {
		return "coach/coachJoin";
	}
	
	@GetMapping("/api/member/idCheck")
    @ResponseBody
    public boolean checkId(@RequestParam("username") String username) {
		boolean result=true;
        try {
        	result=coachService.idCheck(username);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return !result;
    }
	@GetMapping("/api/member/emailCheck")
	@ResponseBody
	public boolean checkEmail(@RequestParam("email") String email) {
		boolean result=true;
		try {
			result=coachService.emailCheck(email);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return !result;
	}
	@RequestMapping("/coachProfile")
	public ModelAndView coachProfile(UsersDTO udto
			,@RequestParam(value = "email", required = false)String mail
			,@RequestParam(value = "kakao_key", defaultValue="")String kakao_key) {
		ModelAndView mav=new ModelAndView();
		int result=0;
		List<Map<String, Object>> majorList=null;
		List<Map<String, Object>> majorRegions=null;
		try {
			result=coachService.coachProfile(udto);
			majorList = coachService.getMajorFields();
			majorRegions = coachService.getMajorRegions();
	        
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(result>0) {
			mav.setViewName("/coach/coachProfile");
			mav.addObject("login_id",udto.getLogin_id());
			mav.addObject("mail",mail);
			mav.addObject("majorList", majorList);
			mav.addObject("majorRegions", majorRegions);
			mav.addObject("kakao_key", kakao_key);
		}else {
			mav.setViewName("redirect:/coachJoin");
		}
		return mav;
	}
	@GetMapping("/api/member/checkNick")
    @ResponseBody
    public boolean checkNick(@RequestParam("nickname") String nickname) {
		boolean result=false;
        try {
        	result=coachService.checkNick(nickname);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return result;
    }
	@GetMapping("/api/coach/minorFields")
	@ResponseBody
	public List<Map<String, Object>> getMinorFields(@RequestParam("majorIdx") int majorIdx) {
	    try {
	        return coachService.getMinorFields(majorIdx);
	    } catch (Exception e) {
	        e.printStackTrace();
	        return null;
	    }
	}
	@GetMapping("/api/coach/minorRegions")
	@ResponseBody
	public List<Map<String, Object>> getMinorRegions(@RequestParam("majorIdx") int majorIdx) {
	    try {
	        return coachService.getMinorRegions(majorIdx);
	    } catch (Exception e) {
	        e.printStackTrace();
	        return null;
	    }
	}
	
	@PostMapping("/coachJoin")
	public ModelAndView coachJoin(CoachDTO cdto,
			@RequestParam(value="login_id") String login_id
//	        @RequestParam(value="uploadPhoto", required = false)MultipartFile uploadPhoto,
//	        @RequestParam(value="uploadVideo", required = false)MultipartFile uploadVideo
	        ) {

	    ModelAndView mav = new ModelAndView();

	    try {
	        int user_idx = coachService.getUsersIdx(login_id);
	        cdto.setUser_idx(user_idx);
	        int result = coachService.coachJoin(cdto);

	        if(result > 0) {
	        	mav.addObject("msg", "코치 회원가입이 완료되었습니다. 로그인 후 이용해주세요.");
	        	mav.addObject("url", "/login");
	        	mav.setViewName("alert");
	        	coachService.activateCoach(user_idx);
	        } else {
	            mav.addObject("msg", "코치 회원가입에 실패했습니다. 다시 시도해주세요.");
	            mav.addObject("url", "/coachJoin");
	            mav.setViewName("alert");
	        }
	        
	    } catch (Exception e) {
	        e.printStackTrace();
	        mav.addObject("msg", "오류가 발생했습니다. 다시 시도해주세요.");
	        mav.addObject("url", "/coachJoin");
	        mav.setViewName("alert");
	    }
	    
	    return mav;
	}
	
	
}
