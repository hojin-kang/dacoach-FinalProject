package com.dacoach.kakaopay;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class PayCancelDTO {
	private String tid;
	private String partner_order_id;
	private String partner_user_id;
	private String canceled_at;
	private String payload;
	private int total;
	private int tax_free;
	private int vat;
	private int point;
	private int discount;
	private int green_deposit;
}
