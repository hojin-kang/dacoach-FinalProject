package com.dacoach.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.dacoach.model.coach.CoachDTO;
import com.dacoach.model.company.CertDTO;
import com.dacoach.model.review.ReviewCoachDTO;
import com.dacoach.service.chat.ChatService;
import com.dacoach.service.coach.CoachService;
import com.dacoach.service.coachSearch.CoachSearchService;
import com.dacoach.service.review.ReviewService;

import jakarta.servlet.http.HttpSession;

@Controller
public class CoachSearchController {
		@Autowired
		private CoachService coachService;
		@Autowired
		private CoachSearchService coachSearchService;
		@Autowired
		private ReviewService reviewService;
		@Autowired
		private ChatService chatService;
		
	 	@GetMapping("/coach/search")
	public ModelAndView coachSearchForm(@RequestParam(value="cp", defaultValue = "1") int cp,
			@RequestParam(value="keyword", defaultValue = "") String keyword,
			@RequestParam(value="sort", defaultValue = "latest") String sort,
			@RequestParam(value="majorField", defaultValue = "0") int majorField,
			@RequestParam(value="minorField", defaultValue = "0") int minorField,
			@RequestParam(value="majorRegion", defaultValue = "0") int majorRegion,
			@RequestParam(value="minorRegion", defaultValue = "0") int minorRegion,
			HttpSession session
			) {
		ModelAndView mav = new ModelAndView();
		//대분류(분야, 지역) 리스트 담기
		List<Map<String, Object>> majorFields = null;
		List<Map<String, Object>> majorRegions = null;
		try {
			majorFields = coachService.getMajorFields();
			majorRegions = coachService.getMajorRegions();
			mav.addObject("majorFields", majorFields);
			mav.addObject("majorRegions", majorRegions);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//코치 검색 결과 담기
		List<CoachDTO> coachList = null;
		try {
			HashMap<String,Object> map = new HashMap<String,Object>();
			map.put("keyword", keyword);
			map.put("majorField", majorField);
			map.put("minorField", minorField);
			map.put("majorRegion", majorRegion);
			map.put("minorRegion", minorRegion);
			map.put("keyword", keyword);
			map.put("sort", sort);
			int loginIdx = session.getAttribute("user_idx") == null ? 0 : (int) session.getAttribute("user_idx");
		    map.put("login_idx", loginIdx);
			coachList = coachSearchService.coachList(cp, map);
			
			mav.addObject("coachList", coachList);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		mav.addObject("sort", sort);
		mav.setViewName("coach/coachSearch");
		return mav;
	}
	 	@GetMapping("/coach/detail")
	 	public ModelAndView coachDetail(@RequestParam(value="user_idx") int target_idx, HttpSession session) {
	 	    ModelAndView mav = new ModelAndView();
	 	    Integer login_idx = (Integer) session.getAttribute("user_idx");
	 	    
	 	    if(login_idx == null) {
	 	        mav.addObject("msg", "로그인 후 이용 가능합니다.").addObject("url", "/login").setViewName("alert");
	 	        return mav;
	 	    }

	 	    try {
	 	        Map<String, Object> map = new HashMap<>();
	 	        map.put("start", 1); map.put("end", 1);
	 	        map.put("keyword", ""); map.put("login_idx", login_idx);
	 	        
	 	        // 현재 상세페이지 코치의 정보를 가져오는 로직 (상태값 포함)
	 	        CoachDTO cdto = coachSearchService.getCoachDetailStatus(target_idx, login_idx);
	 	        List<String> myHashtags = coachSearchService.getCoachHashtags(target_idx);
	 	        List<String> interHashtags = coachSearchService.getInterHashtags(target_idx);
	 	        boolean isLiked = coachSearchService.isLiked(login_idx, target_idx);
	 	        mav.addObject("isLiked", isLiked);
	 	        mav.addObject("myHashtags", myHashtags);
	 	        mav.addObject("interHashtags", interHashtags);
	 	        mav.addObject("dto", cdto);
	 	        List<CertDTO> lists=new ArrayList<CertDTO>();
	 	        lists=coachService.getCoachCertList(target_idx);
	 	        List<CertDTO> certList = new ArrayList<CertDTO>();
	 	        for(int i=0;i<lists.size();i++) {
	 	        	if(lists.get(i).getCert_status().equals("승인")) {
	 	        		certList.add(lists.get(i));
	 	        	}
	 	        }
	 	        mav.addObject("certList", certList);
	 	        //해당 코치 리뷰 리스트
	 	        List<ReviewCoachDTO> reviewList = reviewService.getCoachReviews(target_idx);
	 	        mav.addObject("reviewList", reviewList);
	 	        
	 	        
	 	        mav.setViewName("coach/detail");
	 	    } catch (Exception e) { e.printStackTrace(); }
	 	    return mav;
	 	}
	 	
	 	@GetMapping("/coach/like")
	 	public String likeCoach(@RequestParam("target_idx") int target_idx, HttpSession session) throws Exception {
	 	    Integer me = (Integer) session.getAttribute("user_idx");
	 	    if (me == null) return "redirect:/login";

	 	    coachService.likeCoach(me, target_idx);

	 	    return "redirect:/coach/detail?user_idx=" + target_idx;
	 	}

	 	@GetMapping("/coach/unlike")
	 	public String unlikeCoach(@RequestParam("target_idx") int target_idx, HttpSession session) throws Exception {
	 	    Integer me = (Integer) session.getAttribute("user_idx");
	 	    if (me == null) return "redirect:/login";

	 	    coachService.unlikeCoach(me, target_idx);

	 	    return "redirect:/coach/detail?user_idx=" + target_idx;
	 	}

	 	
	 	
	 	
	 	@PostMapping("/match/apply")
	 	@ResponseBody
	 	public Map<String, Object> applyMatch(@RequestParam String type, @RequestParam int target_idx, HttpSession session) {
	 	    Map<String, Object> result = new HashMap<>();
	 	    Integer me = (Integer) session.getAttribute("user_idx");
	 	    
	 	    // 1. 로그인 체크
	 	    if (me == null) {
	 	        result.put("status", "login_required");
	 	        return result;
	 	    }
	 	    //채팅 및 매칭 수락
	 	    try {
				CoachDTO coach=coachService.getCoachInfo(me);
	 	        if (type.equals("A_CHAT")) {
	 	        	int chatOk=coachSearchService.acceptChat(me, target_idx);
	 	        	//채팅방 개설
	 	        	if(chatOk>0) {
	 	        		chatService.getOrCreateRoom(me, target_idx);
	 	        	}
	 	            result.put("status", "chat_accepted");
	 	            return result;
	 	        }
	 	        if (type.equals("A_MATCH")) {
	 	        	int matchOk=coachSearchService.acceptMatch(me, target_idx);
	 	        	if(matchOk>0) {
	 	        		chatService.getOrCreateRoom(me, target_idx);
	 	        	}
	 	            result.put("status", "match_accepted");
	 	            return result;
	 	        }				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	 	    
	 	    try {
	 	        // 2. 토큰 체크
	 	        CoachDTO coachInfo = coachService.getCoachInfo(me);
	 	        int mytoken = (coachInfo != null) ? coachInfo.getToken_balance() : 0;

	 	        if (type.equals("CHAT") && mytoken < 1) {
	 	            result.put("status", "no_token");
	 	            return result;
	 	        } else if (!type.equals("CHAT") && mytoken < 3) {
	 	            result.put("status", "no_token");
	 	            return result;
	 	        }

	 	        // 3. 신청 로직 실행 (토큰 검증 통과 시에만 실행됨)
	 	        coachSearchService.applyMatchOrChat(me, target_idx, type);
	 	        coachSearchService.useTokens(me, type.equals("CHAT") ? 1 : 3);
	 	        
	 	        // 4. token_history 데이터 삽입
	 	        coachSearchService.addTokenHistory(me, type.equals("CHAT")?"채팅 신청":"매칭 신청", type.equals("CHAT")?-1:-3, type.equals("CHAT")?mytoken-1:mytoken-3);
	 	        result.put("status", "success");

	 	    } catch (Exception e) {
	 	        e.printStackTrace();
	 	        result.put("status", "error"); // 예외 발생 시 사용자에게 에러 알림
	 	    }
	 	    

	 	    
	 	    
	 	    
	 	    return result;
	 	}
	 	
	 // 상세페이지에서 POST로 들어옴 (user_idx 노출 X)
	 	@PostMapping("/coach/allReview")
	 	public String allReviewPost(@RequestParam("user_idx") int target_idx, HttpSession session) {
	 	    session.setAttribute("allReviewTargetIdx", target_idx);
	 	    return "redirect:/coach/allReview"; // cp=1 기본
	 	}

	 	// 페이징 이동은 GET (PageModule이 ?cp= 를 만들어주니까)
	 	@GetMapping("/coach/allReview")
	 	public ModelAndView allReviewGet(
	 	        @RequestParam(value="cp", defaultValue="1") int cp,
	 	        HttpSession session
	 	) {
	 	    ModelAndView mav = new ModelAndView();

	 	    Integer target_idx = (Integer) session.getAttribute("allReviewTargetIdx");
	 	    if (target_idx == null) {
	 	        mav.addObject("msg", "잘못된 접근입니다. 코치 상세에서 다시 들어와주세요.")
	 	           .addObject("url", "/coach/search")
	 	           .setViewName("alert");
	 	        return mav;
	 	    }

	 	    int listSize = 5;   // 한 페이지에 리뷰 n개
	 	    int pageSize = 5;   // 페이지 버튼 n개씩

	 	    int totalCnt = reviewService.getCoachReviewCount(target_idx);

	 	    int start = (cp - 1) * listSize + 1;
	 	    int end = cp * listSize;

	 	    List<ReviewCoachDTO> reviewList = reviewService.getCoachReviewsPaged(target_idx, start, end);

	 	    CoachDTO dto = null;
			try {
				dto = coachService.getCoachInfo(target_idx);
			} catch (Exception e) {
				e.printStackTrace();
			}

	 	    String pageStr = com.dacoach.page.PageModule.makePage("/coach/allReview", totalCnt, listSize, pageSize, cp);

	 	    mav.addObject("dto", dto);
	 	    mav.addObject("reviewList", reviewList);
	 	    mav.addObject("pageStr", pageStr);
	 	    mav.addObject("totalCnt", totalCnt);
	 	    mav.setViewName("coach/allReview");
	 	    return mav;
	 	}

	 	
}
