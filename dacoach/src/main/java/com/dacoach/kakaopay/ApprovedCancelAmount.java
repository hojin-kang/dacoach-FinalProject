package com.dacoach.kakaopay;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
/*이번 요청으로 취소된 내역*/
public class ApprovedCancelAmount {
	private int total;
	private int tax_free;
	private int vat;
	private int point;
	private int discount;
	private int green_deposit;
}
