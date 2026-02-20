package com.dacoach.adminad.service;

import java.util.*;
public interface AdminAdManagementService {
	
	List<Map<String,Object>> getAdRequestList(int startRow,int endRow);
	int sendNotification(int ad_idx,int user_idx,String action);
	int getAdTotalCnt();
}
