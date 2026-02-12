package com.dacoach.service.notification;

import java.util.List;

import com.dacoach.model.notification.NotificationDTO;

public interface NotificationService {

	public List<NotificationDTO> notiList(int user_idx);
	
	public int countUnread(int user_idx);
	
	public int notiDelete(int noti_idx);
	
	public int insertNotification(NotificationDTO dto);
}
