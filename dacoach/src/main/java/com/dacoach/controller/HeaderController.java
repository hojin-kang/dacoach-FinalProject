package com.dacoach.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.dacoach.service.notification.NotificationService;

import jakarta.servlet.http.HttpSession;

@ControllerAdvice
public class HeaderController {

	@Autowired
    private NotificationService notificationService;

    @ModelAttribute("unreadNotiCount")
    public int unreadNotiCount(HttpSession session) {
        Integer usersIdx = (Integer) session.getAttribute("users_idx");
        if (usersIdx == null) return 0;
        return notificationService.countUnread(usersIdx);
    }
}
