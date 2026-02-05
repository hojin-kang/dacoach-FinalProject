package com.dacoach.controller;

import java.util.Map;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import com.dacoach.model.chat.ChatMessageDTO;
import com.dacoach.service.chat.ChatService;

@Controller
public class ChatWsController {

    private final ChatService chatService;
    private final SimpMessagingTemplate messagingTemplate;

    public ChatWsController(ChatService chatService, SimpMessagingTemplate messagingTemplate) {
        this.chatService = chatService;
        this.messagingTemplate = messagingTemplate;
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
}
