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
	public List<NotificationDTO> notiList(int user_idx) {
		List<NotificationDTO> ndtos = notiMapper.notiList(user_idx);
		return ndtos;
	}
	
	@Override
	public int countUnread(int user_idx) {
        return notiMapper.countUnread(user_idx);
    }
	
	@Override
	public int notiDelete(int noti_idx) {
		int result = notiMapper.notiDelete(noti_idx);
		return result;
	}

	@Override
	public int insertNotification(NotificationDTO dto) {
		int result = notiMapper.insertNotification(dto);
		return result;
	}
}
