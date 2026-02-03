package com.dacoach.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.dacoach.model.coach.CoachDTO;
import com.dacoach.model.users.UsersDTO;
import com.dacoach.service.coach.CoachService;

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
	@RequestMapping("/coachProfile")
	public ModelAndView coachProfile(UsersDTO udto,@RequestParam(value = "email", required = false)String mail) {
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
		if(result>=0) {
			mav.setViewName("/coach/coachProfile");
			mav.addObject("login_id",udto.getLogin_id());
			mav.addObject("mail",mail);
			mav.addObject("majorList", majorList);
			mav.addObject("majorRegions", majorRegions);
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
	public ModelAndView coachJoin(CoachDTO cdto,@RequestParam(value="mail")String mail,
			@RequestParam(value="login_id")String login_id) {
	    ModelAndView mav = new ModelAndView();
	    int users_idx=0;
	    try {
			users_idx=coachService.getUsersIdx(login_id);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    cdto.setUsers_idx(users_idx);
	    cdto.setMail(mail);
	    int result=0;
	    try {
			result=coachService.coachJoin(cdto);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    if(result>0) {
	    	mav.setViewName("redirect:/login");
	    }else {
	    	mav.setViewName("redirect:/coachJoin");
	    }
	    
	   return mav;
	}
	
	
}
