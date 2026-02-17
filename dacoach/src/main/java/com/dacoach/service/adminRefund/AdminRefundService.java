package com.dacoach.service.adminRefund;

import java.util.*;

public interface AdminRefundService {

	List<Map<String,Object>> getRefundList();
	String approveRefund(int pay_idx);
}
