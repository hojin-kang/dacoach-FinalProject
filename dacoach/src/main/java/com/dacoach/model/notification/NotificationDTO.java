package com.dacoach.model.notification;

import java.sql.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class NotificationDTO {

	private int noti_idx;
	private int receiver_idx, provider_idx;
	private String noti_type;
	private String content;
	private String is_read;
	private Date created_at;
}
