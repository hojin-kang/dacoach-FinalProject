package com.dacoach.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.dacoach.mapper.membership.MembershipMapper;
import com.dacoach.service.membership.MembershipService;

@Controller
public class MembershipController {
	
	@Autowired
	private MembershipService membershipService;
}
