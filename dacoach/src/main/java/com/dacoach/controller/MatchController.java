package com.dacoach.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.dacoach.model.chat.ChatMessageDTO;
import com.dacoach.model.coach.CoachDTO;
import com.dacoach.model.dicip.DicipDTO;
import com.dacoach.model.schedule.ScheduleDTO;
import com.dacoach.service.chat.ChatService;
import com.dacoach.service.coach.CoachService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
public class MatchController {
	@Autowired
	private CoachService coachService;
	@Autowired
	private ChatService chatService;
	@Autowired
	private SimpMessagingTemplate messagingTemplate;
	
	@GetMapping("/dicipline")
	public ModelAndView dicipline(HttpSession session,
			HttpServletRequest request,
			@RequestParam(value = "target_idx")int target_idx, @RequestParam(value = "roomIdx", defaultValue = "0") int roomIdx) {
		ModelAndView mav = new ModelAndView();
		Integer user=(Integer)session.getAttribute("user_idx");
		int user_idx=0;
		if(user==null) {
			mav.setViewName("redirect:/login");
			return mav;
		}else {
			user_idx=user.intValue();
		}
		if(!chatService.isMatched(user_idx, target_idx).equals("MATCHED")){
			mav.addObject("msg", "매칭된 상대가 아닙니다.");
			mav.addObject("url", "/");
			mav.setViewName("alert");
			return mav;
		}
		
		try {
			CoachDTO cdto = coachService.getCoachInfo(target_idx);
			mav.addObject("cdto", cdto);
			DicipDTO dto = coachService.getDicip(user_idx, target_idx);
			String status = "";
			if(dto==null) {
				//서약서 없을 경우 작성 화면
				status="none";
			}else if(dto.getStatus().equals("N") && dto.getWriter_idx()==user_idx) {
				//내가 작성한 서약서 상대방 미동의 상태
				mav.addObject("dto", dto);
				status="waiting";
			}else if(dto.getStatus().equals("N") && dto.getWriter_idx()==target_idx) {
				//상대방이 작성한 서약서 동의 대기 상태
				mav.addObject("dto", dto);
				status="wait";
			}else {
				//서약서 작성 및 동의 완료 상태
				mav.addObject("dto", dto);
				status="done";
			}
			mav.addObject("roomIdx", roomIdx);
			mav.addObject("target_idx", target_idx);
			mav.addObject("status", status);
			mav.setViewName("match/dicipline");
		} catch (Exception e) {
			mav.addObject("msg", "서약서 정보를 불러오는 중 오류가 발생했습니다.");
			mav.addObject("url", "/");
			mav.setViewName("alert");
			e.printStackTrace();
		}
		
		return mav;
	}
	@PostMapping("/dicipline/submit")
	public ModelAndView submitDicip(HttpSession session, DicipDTO dto, @RequestParam(value = "roomIdx", defaultValue = "0") int roomIdx) {
		ModelAndView mav = new ModelAndView();
		Integer user=(Integer)session.getAttribute("user_idx");
		int user_idx=0;
		String myName="";
		if(user==null) {
			mav.setViewName("redirect:/login");
			return mav;
		}else {
			user_idx=user.intValue();
			dto.setWriter_idx(user_idx);
			dto.setStatus("N");
			try {
				myName=coachService.getUserInfo(user_idx).getUser_name();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	
		int result=0;
		try {
			result = coachService.writeDicip(dto);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(result>0) {
			mav.addObject("msg", "서약서가 성공적으로 제출되었습니다.");
			mav.addObject("url", "/dicipline?target_idx="+dto.getReceiver_idx());
			mav.setViewName("alert");
			if(roomIdx!=0) {
	 	      	 // 1. ChatMessageDTO 완벽하게 수동 조립
	 	   	    ChatMessageDTO in = new ChatMessageDTO();
	 	   	    in.setRoomIdx(roomIdx);
	 	   	    in.setSenderIdx(0);
	 	   	    in.setMessage(myName+"님이 서약서를 제출했습니다.");
	 	   	    
	 	   	    // [추가] 만약 서비스에서 시간을 자동으로 안 넣어준다면 수동으로 세팅
	 	   	    String nowTs = java.time.LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
	 	   	    in.setTs(nowTs); 
	 	   	    
	 	   	    // 2. 파일 저장 실행
	 	   	    // 이 메서드가 ChatWsController에서 성공했던 바로 그 메서드이므로 동일하게 동작해야 합니다.
	 	   	    chatService.saveAndBuildBroadcast(in);

	 	   	    // 3. 실시간 알림 (상대방의 UI를 즉시 비활성화시키기 위함)
	 	   	    Map<String, Object> matchNotice = new HashMap<>();
	 	   	    matchNotice.put("type", "AGREESUBMIT");
	 	   	    matchNotice.put("senderIdx", 0); 
	 	   	    matchNotice.put("message",myName+"님이 서약서를 제출했습니다.");
	 	   	    matchNotice.put("ts", nowTs);
	 	   	    messagingTemplate.convertAndSend("/topic/room." + roomIdx, (Object) matchNotice);
	 	        }
		}
		return mav;
	}
	@PostMapping("/dicipline/agree")
	public ModelAndView agreeDicip(HttpSession session, @RequestParam(value="agreement_idx", defaultValue = "0") int agreement_idx,
			@RequestParam(value = "roomIdx", defaultValue = "0") int roomIdx) {
		ModelAndView mav = new ModelAndView();
		Integer user=(Integer)session.getAttribute("user_idx");
		int user_idx=0;
		String myName="";
		if(user==null) {
			mav.setViewName("redirect:/login");
			return mav;
		}else {
			user_idx=user.intValue();
			try {
				myName=coachService.getUserInfo(user_idx).getUser_name();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		int result=0;
		try {
			result = coachService.agreeDicip(agreement_idx);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(result>0) {
			mav.addObject("msg", "서약서에 동의하였습니다.");
			mav.addObject("url", "/chat");
			mav.setViewName("alert");
			if(roomIdx!=0) {
	 	      	 // 1. ChatMessageDTO 완벽하게 수동 조립
	 	   	    ChatMessageDTO in = new ChatMessageDTO();
	 	   	    in.setRoomIdx(roomIdx);
	 	   	    in.setSenderIdx(0);
	 	   	    in.setMessage(myName+"님이 서약서에 동의했습니다.");
	 	   	    
	 	   	    // [추가] 만약 서비스에서 시간을 자동으로 안 넣어준다면 수동으로 세팅
	 	   	    String nowTs = java.time.LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
	 	   	    in.setTs(nowTs); 
	 	   	    
	 	   	    // 2. 파일 저장 실행
	 	   	    // 이 메서드가 ChatWsController에서 성공했던 바로 그 메서드이므로 동일하게 동작해야 합니다.
	 	   	    chatService.saveAndBuildBroadcast(in);

	 	   	    // 3. 실시간 알림 (상대방의 UI를 즉시 비활성화시키기 위함)
	 	   	    Map<String, Object> matchNotice = new HashMap<>();
	 	   	    matchNotice.put("type", "AGREEACCEPT");
	 	   	    matchNotice.put("senderIdx", 0); 
	 	   	    matchNotice.put("message",myName+"님이 서약서에 동의했습니다.");
	 	   	    matchNotice.put("ts", nowTs);
	 	   	    messagingTemplate.convertAndSend("/topic/room." + roomIdx, (Object) matchNotice);
	 	        }
		}else {
			mav.addObject("msg", "서약서 동의에 실패하였습니다.");
			mav.addObject("url", "/chat");
			mav.setViewName("alert");
		}
		return mav;
	}
	@GetMapping("/schedule/create")
	public ModelAndView createSchedule(HttpSession session, @RequestParam(value="agreement_idx", defaultValue = "0") int agreement_idx,
			@RequestParam(value = "roomIdx", defaultValue = "0") int roomIdx) {
		ModelAndView mav = new ModelAndView();
		Integer user=(Integer)session.getAttribute("user_idx");
		int user_idx=0;
		if(user==null) {
			mav.setViewName("redirect:/login");
			return mav;
		}
		mav.addObject("agreement_idx", agreement_idx);
		mav.addObject("roomIdx", roomIdx);
		
		
		mav.setViewName("coach/createSchedule");
		return mav;
	}
	@PostMapping("/schedule/create")
	public ModelAndView createSchduleSubmit(HttpSession session,
			ScheduleDTO dto, @RequestParam(value = "roomIdx", defaultValue = "0") int roomIdx) {
		ModelAndView mav = new ModelAndView();
		Integer user=(Integer)session.getAttribute("user_idx");
		int user_idx=0;
		String myName="";
		if(user==null) {
			mav.setViewName("redirect:/login");
			return mav;
		}else {
			user_idx=user.intValue();
			try {
				myName=coachService.getUserInfo(user_idx).getUser_name();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		int result=0;
		try {
			result = coachService.createSchedule(dto);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(result>0) {
			mav.addObject("url", "/schedule/list?agreement_idx="+dto.getAgreement_idx());
			mav.addObject("msg", "일정이 성공적으로 등록되었습니다.");
			mav.setViewName("alert");
			if(roomIdx!=0) {
	 	      	 // 1. ChatMessageDTO 완벽하게 수동 조립
	 	   	    ChatMessageDTO in = new ChatMessageDTO();
	 	   	    in.setRoomIdx(roomIdx);
	 	   	    in.setSenderIdx(0);
	 	   	    in.setMessage(myName+"님이 새로운 일정을 등록했습니다.");
	 	   	    
	 	   	    // [추가] 만약 서비스에서 시간을 자동으로 안 넣어준다면 수동으로 세팅
	 	   	    String nowTs = java.time.LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
	 	   	    in.setTs(nowTs); 
	 	   	    
	 	   	    // 2. 파일 저장 실행
	 	   	    // 이 메서드가 ChatWsController에서 성공했던 바로 그 메서드이므로 동일하게 동작해야 합니다.
	 	   	    chatService.saveAndBuildBroadcast(in);

	 	   	    // 3. 실시간 알림 (상대방의 UI를 즉시 비활성화시키기 위함)
	 	   	    Map<String, Object> matchNotice = new HashMap<>();
	 	   	    matchNotice.put("type", "SCHEDULE");
	 	   	    matchNotice.put("senderIdx", 0); 
	 	   	    matchNotice.put("message",myName+"님이 새로운 일정을 등록했습니다.");
	 	   	    matchNotice.put("ts", nowTs);
	 	   	    messagingTemplate.convertAndSend("/topic/room." + roomIdx, (Object) matchNotice);
	 	        }
		}else {
			mav.addObject("url", "/schedule/create?agreement_idx="+dto.getAgreement_idx());
			mav.addObject("msg", "일정 등록에 실패하였습니다.");
			mav.setViewName("alert");
		}
		
		return mav;
	}
	@GetMapping("/schedule/list")
	public ModelAndView scheduleList(HttpSession session, @RequestParam(value="agreement_idx", defaultValue = "0") int agreement_idx) {
		ModelAndView mav = new ModelAndView();
		Integer user=(Integer)session.getAttribute("user_idx");
		if(user==null) {
			mav.setViewName("redirect:/login");
			return mav;
		}
		try {
			List<ScheduleDTO> allSchedule = coachService.getSchedule(agreement_idx);
			if(allSchedule!=null && allSchedule.size()>0) {
				List<ScheduleDTO> complete = allSchedule.stream().filter(s -> s.getEnd_date().before(new java.util.Date())).toList();
				List<ScheduleDTO> ongoing = allSchedule.stream().filter(s -> s.getStart_date().before(new java.util.Date()) && s.getEnd_date().after(new java.util.Date())).toList();
				List<ScheduleDTO> upcoming = allSchedule.stream().filter(s -> s.getEnd_date().after(new java.util.Date())).toList();
				mav.addObject("complete", complete);
				mav.addObject("ongoing", ongoing);
				mav.addObject("upcoming", upcoming);
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			mav.addObject("msg", "일정 정보를 불러오는 중 오류가 발생했습니다.");
			mav.addObject("url", "/");
			mav.setViewName("alert");
			return mav;
		}
		
		mav.setViewName("coach/scheduleList");
		return mav;
	}
}
