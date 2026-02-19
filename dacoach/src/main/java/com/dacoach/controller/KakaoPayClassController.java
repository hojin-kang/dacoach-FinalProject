package com.dacoach.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.dacoach.kakaopay.KakaoApproveResponse;
import com.dacoach.kakaopay.KakaoPayService;
import com.dacoach.kakaopay.KakaoReadyResponse;
import com.dacoach.service.coachClasses.CoachClassService;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/kakaopay/class")
public class KakaoPayClassController {

	private final KakaoPayService kakaoPayService;
	private final CoachClassService classService;

	@PostMapping("/ready")
	@ResponseBody
	public ResponseEntity<KakaoReadyResponse> ready(@RequestBody Map<String, Object> params, HttpSession session) {

		Integer user_idx = (Integer) session.getAttribute("user_idx");
		if (user_idx == null || user_idx == 0)
			return ResponseEntity.status(401).build();

		params.put("partner_user_id", String.valueOf(user_idx));

		KakaoReadyResponse res = kakaoPayService.kakaoPayReady(params);

		// 결제 완료 후 success에서 사용할 정보 세션에 저장
		session.setAttribute("class_id", params.get("class_id"));
		session.setAttribute("enrollment_date", params.get("enrollment_date"));

		return ResponseEntity.ok(res);
	}

	@GetMapping("/success")
	public String success(@RequestParam("pg_token") String pgToken, HttpSession session) {

		Integer user_idx = (Integer) session.getAttribute("user_idx");
		if (user_idx == null || user_idx == 0)
			return "redirect:/needLogin";

		Object classIdObj = session.getAttribute("class_id");
		Object enrollmentDateObj = session.getAttribute("enrollment_date");
		if (classIdObj == null || enrollmentDateObj == null)
			return "redirect:/coach/classList?result=invalid";

		int class_id = Integer.parseInt(classIdObj.toString());
		String enrollment_date = enrollmentDateObj.toString();

		KakaoApproveResponse approve = kakaoPayService.approveResponse(pgToken, null);
		if (approve == null)
			return "redirect:/coach/classEnrollment?classId=" + class_id + "&result=approve_fail";

		try {
			classService.enrollClassAfterPay(class_id, user_idx, enrollment_date, approve);
		} catch (Exception e) {
			e.printStackTrace();
			return "redirect:/coach/classEnrollment?classId=" + class_id + "&result=db_fail";
		}

		session.removeAttribute("class_id");
		session.removeAttribute("enrollment_date");

		return "redirect:/coach/myEnrollment?result=success";
	}

	@GetMapping("/fail")
	public String fail(HttpSession session) {
		Object classIdObj = session.getAttribute("class_id");
		String classId = classIdObj != null ? classIdObj.toString() : "";
		return "redirect:/coach/classEnrollment?classId=" + classId + "&result=fail";
	}

	@GetMapping("/cancel")
	public String cancel(HttpSession session) {
		Object classIdObj = session.getAttribute("class_id");
		String classId = classIdObj != null ? classIdObj.toString() : "";
		return "redirect:/coach/classEnrollment?classId=" + classId + "&result=cancel";
	}
}