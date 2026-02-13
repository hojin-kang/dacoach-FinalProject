package com.dacoach.kakaopay;

import java.sql.Date;

import lombok.Data;

@Data
public class KakaoReadyResponse {
	private String tid;
	private String next_redirect_app_url;
	private String next_redirect_mobile_url;
	private String next_redirect_pc_url;
	private Date created_at;
}
