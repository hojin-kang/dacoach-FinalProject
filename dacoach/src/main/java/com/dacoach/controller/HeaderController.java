package com.dacoach.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.dacoach.service.chat.ChatService;
import com.dacoach.service.notification.NotificationService;

import jakarta.servlet.http.HttpSession;

@ControllerAdvice
public class HeaderController {

	@Autowired
    private NotificationService notificationService;
	
	@Autowired
	private ChatService chatService;

    @ModelAttribute("unreadNotiCount")
    public int unreadNotiCount(HttpSession session) {
        Integer usersIdx = (Integer) session.getAttribute("user_idx");
        if (usersIdx == null) return 0;
        return notificationService.countUnread(usersIdx);
    }
    
    @ModelAttribute("unreadChatCount")
    public int unreadChat(HttpSession session) {
        Integer usersIdx = (Integer) session.getAttribute("user_idx");
        if (usersIdx == null) return 0;
        return chatService.countUnread(usersIdx);
    }
}
