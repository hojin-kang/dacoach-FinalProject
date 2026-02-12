package com.dacoach.kakaopay;

import java.sql.Date;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class KakaoCancelResponse {
	private String aid;
	private String tid;
	private String cid;
	private String status;	//CANCEL_PAYMENT로 상태확인
	private String partner_order_id;
	private String partner_user_id;
	private String payment_method_type;
	private Amount amount;
	private ApprovedCancelAmount approved_cancel_amount;
	private CancelAvailableAmount cancel_available_amount;
	private String item_name;
	private String item_code;
	private int quantity;
	private Date created_at;
	private Date approved_at;
	private Date canceled_at;
	private String payload;
}
