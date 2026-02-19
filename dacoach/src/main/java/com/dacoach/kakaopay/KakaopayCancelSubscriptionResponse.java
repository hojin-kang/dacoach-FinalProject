package com.dacoach.kakaopay;

import java.sql.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class KakaopayCancelSubscriptionResponse {
	
	private boolean available;
	private String cid;
	private String sid;
	private String status;
	private String pay_method_type;
	private String item_name;
	private Date created_at;
	private Date last_approved_at;
	private Date inactivated_at;
	private String use_point_status;
	
}
