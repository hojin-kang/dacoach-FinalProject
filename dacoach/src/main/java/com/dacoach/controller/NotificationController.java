package com.dacoach.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import com.dacoach.model.notification.NotificationDTO;
import com.dacoach.service.chat.ChatService;
import com.dacoach.service.notification.NotificationService;

import jakarta.servlet.http.HttpSession;


@Controller
public class NotificationController {
	
	@Autowired
    private NotificationService nService;
	
	@Autowired
	private ChatService chatService;

	@GetMapping("/notification")
	public ModelAndView notification(HttpSession session) {
		ModelAndView mav = new ModelAndView();
		if(session.getAttribute("user_idx")==null || (Integer)session.getAttribute("user_idx")==0) {
			mav.setViewName("/needLogin");
			return mav;
		}
		int user_idx = (Integer)session.getAttribute("user_idx");
		List<NotificationDTO> ndtos = null;
		try {
			ndtos = nService.notiList(user_idx);
		} catch (Exception e) {
			e.printStackTrace();
		}
		mav.addObject("ndtos", ndtos);
		mav.setViewName("/coach/notification");
		return mav;
	}
	
	@PostMapping("/notiDelete")
	public ModelAndView notiDelete(Integer noti_idx) {
		ModelAndView mav = new ModelAndView();
		try {
			nService.notiDelete(noti_idx);
		} catch (Exception e) {
			e.printStackTrace();
		}
		mav.setViewName("redirect:/notification");
		return mav;
	}
	
	@GetMapping("/notification/open")
	public String openNoti(int noti_idx, HttpSession session) {
	    if (session.getAttribute("user_idx") == null || (Integer) session.getAttribute("user_idx") == 0) {
	        return "redirect:/needLogin";
	    }
	    int user_idx = (Integer) session.getAttribute("user_idx");

	    // 내 알림 맞는지 확인용 조회 (새로 추가)
	    NotificationDTO n = nService.getNotiForUser(noti_idx, user_idx);
	    if (n == null) return "redirect:/notification";

	    String type = n.getNoti_type();
	    int provider = n.getProvider_idx();

	    switch (type) {

	        // ===== coach 입장 =====
	        case "CHAT_REQUEST":
	        case "CHAT_ACCEPT":
	        case "MATCH_REQUEST":
	        case "MATCH_ACCEPT":
	            // 발신자 코치 상세페이지로 이동
	            return "redirect:/coach/detail?user_idx=" + provider;

	        case "CLASS_NOTICE": { // 발신자 클래스 채팅방으로 이동
	        	Integer roomIdx = chatService.findRoom(user_idx, provider);

	            if (roomIdx == null) {
	                // 채팅방 없으면 알림 목록으로 복귀
	                return "redirect:/notification";
	            }

	            return "redirect:/chat/room/" + roomIdx + "?tab=COMPANY";
	        }

	        case "REVIEW":
	            // 마이페이지 나의 리뷰(받은 리뷰)
	            return "redirect:/myReview#received";

	            
	        // ===== company 입장 =====
	        case "QNA": {
	        	Integer roomIdx = chatService.findRoom(user_idx, provider);

	            if (roomIdx == null) {
	                // 채팅방 없으면 알림 목록으로 복귀
	                return "redirect:/notification";
	            }

	            return "redirect:/chat/room/" + roomIdx;
	        }

	        case "ENROLLMENT":
	            // 클래스 수강생 목록
	            return "redirect:/company/classes/classStatus";

	        default:
	            // 공지/닭꼬치 지급일 경우
	            return "redirect:/notification";
	    }
	}
}
