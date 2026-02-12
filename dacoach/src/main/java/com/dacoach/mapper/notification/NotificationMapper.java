package com.dacoach.mapper.notification;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.dacoach.model.notification.NotificationDTO;

@Mapper
public interface NotificationMapper {
	
	public List<NotificationDTO> notiList(int user_idx);
	
	public int countUnread(int user_idx);
	
	public int notiDelete(int noti_idx);
	
	public int insertNotification(NotificationDTO dto);
}
