package com.dacoach.mapper.notification;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.dacoach.model.notification.NotificationDTO;

@Mapper
public interface NotificationMapper {
	
	public List<NotificationDTO> notiList(int user_idx);
	
	public int countUnread(int user_idx);
	
	public int notiDelete(int noti_idx);
	
	public int insertNotification(NotificationDTO dto);
	
	NotificationDTO getNotiForUser(@Param("noti_idx") int noti_idx, @Param("user_idx") int user_idx);
}
