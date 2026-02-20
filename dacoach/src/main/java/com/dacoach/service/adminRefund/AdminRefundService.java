package com.dacoach.service.adminRefund;

import java.util.*;

public interface AdminRefundService {

	List<Map<String,Object>> getRefundList(int startRow,int endRow);
	String approveRefund(int pay_idx);
	int getRefundTotalCnt();
}
