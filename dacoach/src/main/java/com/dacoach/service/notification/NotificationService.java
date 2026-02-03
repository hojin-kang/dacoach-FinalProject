package com.dacoach.service.notification;

import java.util.List;

import com.dacoach.model.notification.NotificationDTO;

public interface NotificationService {

	public List<NotificationDTO> notiList(int users_idx);
	
	public int countUnread(int users_idx);
}
