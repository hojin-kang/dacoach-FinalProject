package com.dacoach.kakaopay;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
/*지금까지 취소 요청으로 취소된 누적 내역*/
public class CanceledAmount {
	
	private int total;
	private int tax_free;
	private int vat;
	private int point;
	private int discount;
	private int green_deposit;
}

