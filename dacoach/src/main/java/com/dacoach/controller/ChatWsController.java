package com.dacoach.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import com.dacoach.model.chat.ChatMessageDTO;
import com.dacoach.model.chat.ChatRoomDTO;
import com.dacoach.model.notification.NotificationDTO;
import com.dacoach.service.chat.ChatService;
import com.dacoach.service.company.CompanyService;
import com.dacoach.service.notification.NotificationService;

@Controller
public class ChatWsController {
	
	
	private final CompanyService companyService;
    private final ChatService chatService;
    private final SimpMessagingTemplate messagingTemplate;
    @Autowired
    private NotificationService nService;

    public ChatWsController(ChatService chatService, SimpMessagingTemplate messagingTemplate,CompanyService companyService) {
        this.chatService = chatService;
        this.messagingTemplate = messagingTemplate;
        this.companyService=companyService;
    }

    @MessageMapping("/chat.send")
    public void send(@Payload ChatMessageDTO payload, SimpMessageHeaderAccessor accessor) {

        Map<String, Object> sessionAttrs = accessor.getSessionAttributes();
        if (sessionAttrs == null || sessionAttrs.get("user_idx") == null) return;

        int senderIdx = (Integer) sessionAttrs.get("user_idx");
        
        // payload에서 roomIdx가 null이면 그냥 컷
        if (payload == null || payload.getRoomIdx() == null) return;

        ChatMessageDTO in = new ChatMessageDTO();
        in.setRoomIdx(payload.getRoomIdx());
        in.setSenderIdx(senderIdx);              // 서버가 박음
        in.setMessage(payload.getMessage());
        
        ChatMessageDTO out = chatService.saveAndBuildBroadcast(in);
        if (out == null) return;

        messagingTemplate.convertAndSend("/topic/room." + out.getRoomIdx(), out);
    }
    
    @MessageMapping("/class.send")
    public void classSend(@Payload ChatMessageDTO payload, SimpMessageHeaderAccessor accessor) {

        Map<String, Object> sessionAttrs = accessor.getSessionAttributes();
        if (sessionAttrs == null || sessionAttrs.get("user_idx") == null) return;

        int senderIdx = (Integer) sessionAttrs.get("user_idx");
        
        // payload에서 roomIdx가 null이면 그냥 컷
        if (payload == null || payload.getRoomIdx() == null) return;

        ChatMessageDTO in = new ChatMessageDTO();
        in.setRoomIdx(payload.getRoomIdx());
        in.setSenderIdx(senderIdx);              // 서버가 박음
        in.setMessage(payload.getMessage());
        
        ChatMessageDTO out = companyService.saveAndBuildBroadcast(in);
        if (out == null) return;

        messagingTemplate.convertAndSend("/topic/room." + out.getRoomIdx(), out);
        
        try {
			ChatRoomDTO roomDto= companyService.getClassRoom(payload.getRoomIdx());
		if(roomDto.getUser2Idx()>0) {
			NotificationDTO n=new NotificationDTO();
	        n.setReceiver_idx(roomDto.getUser2Idx());
	        n.setProvider_idx(roomDto.getUser1Idx());
	        n.setNoti_type("CLASS_NOTICE");
	        n.setContent("수강하시는 클래스에서 새 공지사항이 있습니다");
	        nService.insertNotification(n);
		}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
    }
}
