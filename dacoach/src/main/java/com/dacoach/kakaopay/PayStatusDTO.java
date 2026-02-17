package com.dacoach.kakaopay;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PayStatusDTO {
	
	private String tid;
	private String partner_order_id;
	private String partner_user_id;
	private String status;
	private String payload;
}
