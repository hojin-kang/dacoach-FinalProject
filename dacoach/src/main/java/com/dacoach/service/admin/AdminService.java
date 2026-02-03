package com.dacoach.service.admin;

import java.util.*;


public interface AdminService {
	List<Map<String,Object>> getCoachList() throws Exception;
	public int getCertCount() throws Exception;
	List<Map<String,Object>> getCoachDetail(int coachidx) throws Exception;
}
