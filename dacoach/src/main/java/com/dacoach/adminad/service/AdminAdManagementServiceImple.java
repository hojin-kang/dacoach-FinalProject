package com.dacoach.adminad.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dacoach.mapper.adminAd.AdminAdManagementMapper;
import com.dacoach.model.notification.NotificationDTO;
import com.dacoach.service.notification.NotificationService;

@Service
public class AdminAdManagementServiceImple implements AdminAdManagementService {

	@Autowired
	AdminAdManagementMapper admapper;

	@Autowired
	NotificationService notificationService;

	@Override
	public List<Map<String, Object>> getAdRequestList(int startRow,int endRow) {
		return admapper.getAdRequestList(startRow,endRow);
	}
	

	@Override
	public int sendNotification(int ad_idx, int user_idx, String action) {
		NotificationDTO dto = new NotificationDTO();
		dto.setProvider_idx(1);
		dto.setReceiver_idx(user_idx);
		dto.setNoti_type("AD");
		int result = 0;
		if ("APPROVE".equals(action)) {
			dto.setContent("신청하신 광고가 승인되었습니다. 알림 창에서 확인 가능합니다.");
			result = notificationService.insertNotification(dto);
		} else if ("REJECT".equals(action)) {
			dto.setContent("신청하신 광고가 반려되었습니다. 1:1 문의 부탁드립니다.");
			notificationService.insertNotification(dto);
			result = admapper.deleteAdRequest(ad_idx);
		}
		// System.out.println("isread=" +dto.getIs_read());

		return result;
	}

	@Override
	public int getAdTotalCnt() {
		return admapper.getAdTotalCnt();
	}

}
