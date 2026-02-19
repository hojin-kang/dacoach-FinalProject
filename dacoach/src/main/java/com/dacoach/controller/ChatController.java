package com.dacoach.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.dacoach.model.chat.ChatMessageDTO;
import com.dacoach.model.chat.ChatRoomDTO;
import com.dacoach.model.coach.CoachDTO;
import com.dacoach.model.dicip.DicipDTO;
import com.dacoach.service.chat.ChatService;
import com.dacoach.service.coach.CoachService;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class ChatController {
	
	private final ChatService chatService;
	@Autowired
	private CoachService coachService;
	@Autowired
	private SimpMessagingTemplate messagingTemplate;

	@GetMapping("/chat")
	public String chatMain(HttpSession session, Model model,
			@RequestParam(value = "tab", required = false) String tab) {

		Integer my = (Integer) session.getAttribute("user_idx");
		if (my == null)
			return "redirect:/login"; 
		
		model.addAttribute("rooms", chatService.listRooms(my));
		model.addAttribute("myIdx", my);
		model.addAttribute("activeRoom", null);
		model.addAttribute("activeRoomIdx", null);
		model.addAttribute("tab", tab);

		return "chat/chatRoomList";
	}

	@GetMapping("/chat/start")
	public String startChat(@RequestParam int targetIdx, @RequestParam(value = "tab", required = false) String tab,
			HttpSession session) {

		Integer my = (Integer) session.getAttribute("user_idx");
		if (my == null)
			return "redirect:/login";

		int roomIdx = chatService.getOrCreateRoom(my, targetIdx);

		if (tab != null && !tab.isBlank()) {
			return "redirect:/chat/room/" + roomIdx + "?tab=" + tab;
		}
		return "redirect:/chat/room/" + roomIdx;
	}

	@GetMapping("/chat/room/{roomIdx}")
	public String chatRoom(@PathVariable int roomIdx, @RequestParam(value = "tab", required = false) String tab,
			HttpSession session, Model model) {
		
		Integer my = (Integer) session.getAttribute("user_idx");
		if (my == null)
			return "redirect:/login";

		model.addAttribute("rooms", chatService.listRooms(my));
		ChatRoomDTO activeRoom = chatService.selectRoomByIdx(roomIdx, my);
		
		int target_idx=activeRoom.getOtherIdx();
		
		boolean isLeft=chatService.isLeft(roomIdx, my);
		String status=chatService.isMatched(my, target_idx);
		
		int user_idx=my.intValue();
		try {
			DicipDTO dto = coachService.getDicip(user_idx, target_idx);
			if(dto!=null && dto.getStatus().equals("Y")) {
				model.addAttribute("agreement_idx", dto.getAgreement_idx());
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		model.addAttribute("isLeft", isLeft);
		model.addAttribute("activeRoom", activeRoom);
		model.addAttribute("activeRoomIdx", roomIdx);
		model.addAttribute("myIdx", my);
		model.addAttribute("target_idx", target_idx);
		model.addAttribute("status", status);
		model.addAttribute("tab", tab);
		return "chat/chatRoomList";
	}

	@GetMapping(value = "/chat/messages/{roomIdx}", produces = "text/plain;charset=UTF-8")
	@ResponseBody
	public String messages(@PathVariable int roomIdx, HttpSession session) {
		
		Integer my = (Integer) session.getAttribute("user_idx");
		if (my == null)
			return "";
		return chatService.loadMessagesRaw(roomIdx, my);
	}
	
	
	// 채팅방 나가기
	@PostMapping("/chat/room/{roomIdx}/leave")
	@ResponseBody
	public String leaveRoom(@PathVariable int roomIdx, HttpSession session){
	    Integer my = (Integer) session.getAttribute("user_idx");
	    if(my == null) return "NOLOGIN";
	    
	    ChatRoomDTO room=chatService.selectRoomByIdx(roomIdx, my);
	    String status=chatService.isMatched(my, room.getOtherIdx());
	    
	 // 1. ChatMessageDTO 완벽하게 수동 조립
	    ChatMessageDTO in = new ChatMessageDTO();
	    in.setRoomIdx(roomIdx);
	    in.setSenderIdx(0); // 시스템 메시지용 고유 번호
	    in.setMessage("상대방이 채팅방을 나갔습니다.");
	    
	    // [추가] 만약 서비스에서 시간을 자동으로 안 넣어준다면 수동으로 세팅
	    String nowTs = java.time.LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
	    in.setTs(nowTs); 
	    
	    // 2. 파일 저장 실행
	    // 이 메서드가 ChatWsController에서 성공했던 바로 그 메서드이므로 동일하게 동작해야 합니다.
	    chatService.saveAndBuildBroadcast(in);

	    // 3. 실시간 알림 (상대방의 UI를 즉시 비활성화시키기 위함)
	    Map<String, Object> leaveNotice = new HashMap<>();
	    leaveNotice.put("type", "LEAVE");
	    leaveNotice.put("senderIdx", my); 
	    leaveNotice.put("message", "상대방이 채팅방을 나갔습니다.");
	    leaveNotice.put("ts", nowTs);
	    messagingTemplate.convertAndSend("/topic/room." + roomIdx, (Object) leaveNotice);

	    // 4. 퇴장 처리
	    boolean ok = chatService.leaveRoom(roomIdx, my);
	    chatService.updateChatStatus(roomIdx);
	    
	    int result=chatService.deleteChat(roomIdx);
	    if (result > 0) {
	    	
	        String baseDir = "C:/student_java/dacoach/dacoach/uploads/chats";
	        String fileName = "chat_" + room.getUser1Idx() + "_" + room.getUser2Idx();
	        String fileName2 = "chat_" + room.getUser2Idx() + "_" + room.getUser1Idx();
	        
	        try {
	            Path filePath = Paths.get(baseDir, fileName);
	            Path filePath2 = Paths.get(baseDir, fileName2);
	            Files.deleteIfExists(filePath);
	            Files.deleteIfExists(filePath2);
	        } catch (IOException e) {
	            System.err.println("파일 삭제 중 오류 발생: " + e.getMessage());
	        }
	        int delete=chatService.deleteMatch(my, room.getOtherIdx());
	        if(delete > 0) {
	        	try {
					int deleteAgree=coachService.deleteAgree(my, room.getOtherIdx());
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	        }
	        
	        
	    }
	    
		try {
			CoachDTO dto = coachService.getCoachInfo(room.getOtherIdx());
			if(dto!=null && dto.getUser_idx()==room.getOtherIdx() && status.equals("MATCHED")) {
	        	session.setAttribute("reviewDispo", true);
	            long expiryTime = System.currentTimeMillis() + (60 * 60 * 1000);
	            session.setAttribute("reviewExpiry", expiryTime);
	            return ""+room.getOtherIdx();
	        }
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    
        
	    return ok ? "OK" : "FAIL";
	}
}
