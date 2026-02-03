package com.dacoach.service.notification;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dacoach.mapper.notification.NotificationMapper;
import com.dacoach.model.notification.NotificationDTO;

@Service
public class NotificationServiceImple implements NotificationService {

	@Autowired
	private NotificationMapper notiMapper;
	
	@Override
	public List<NotificationDTO> notiList(int users_idx) {
		List<NotificationDTO> ndtos = notiMapper.notiList(users_idx);
		return ndtos;
	}
	
	@Override
	public int countUnread(int users_idx) {
        return notiMapper.countUnread(users_idx);
    }
}
