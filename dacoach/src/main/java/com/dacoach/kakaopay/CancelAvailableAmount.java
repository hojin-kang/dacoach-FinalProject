package com.dacoach.kakaopay;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
/*취소 가능한 내역*/
public class CancelAvailableAmount {
	
	private int total;
	private int tax_free;
	private int vat;
	private int point;
	private int discount;
	private int green_deposit;
}
