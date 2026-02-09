package com.dacoach.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.dacoach.kakaopay.KakaoApproveResponse;
import com.dacoach.kakaopay.KakaoPayService;
import com.dacoach.kakaopay.KakaoReadyResponse;
import com.dacoach.service.token.TokenService;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/kakaopay/token")
public class KakaoPayController {

    private final KakaoPayService kakaoPayService;
    private final TokenService tokenService;

    @PostMapping("/ready")
    @ResponseBody
    public ResponseEntity<KakaoReadyResponse> ready(@RequestBody Map<String, Object> params, HttpSession session) {

        Integer user_idx = (Integer) session.getAttribute("user_idx");
        if (user_idx == null || user_idx == 0) return ResponseEntity.status(401).build();

        // 보안: partner_user_id는 세션 기준으로 덮어쓰기
        params.put("partner_user_id", String.valueOf(user_idx));

        KakaoReadyResponse res = kakaoPayService.kakaoPayReady(params);

        // ✅ success에서 사용할 토큰 수량만 세션에 저장
        session.setAttribute("token_qty", params.get("quantity"));

        return ResponseEntity.ok(res);
    }

    @GetMapping("/success")
    public String success(@RequestParam("pg_token") String pgToken, HttpSession session) {

        Integer user_idx = (Integer) session.getAttribute("user_idx");
        if (user_idx == null || user_idx == 0) return "redirect:/needLogin";

        Object qtyObj = session.getAttribute("token_qty");
        if (qtyObj == null) return "redirect:/tokenChargeForm?result=invalid";

        int qty = Integer.parseInt(qtyObj.toString());

        // ✅ KakaoPayService 그대로 사용 (내부 kakaoReady 필드에 의존)
        KakaoApproveResponse approve = kakaoPayService.approveResponse(pgToken);
        if (approve == null) return "redirect:/tokenChargeForm?result=approve_fail";

        try {
            tokenService.chargeTokenAfterPay(user_idx, qty);
        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/tokenChargeForm?result=db_fail";
        }

        session.removeAttribute("token_qty");

        return "redirect:/tokenHistory";
    }

    @GetMapping("/fail")
    public String fail() {
        return "redirect:/tokenChargeForm?result=fail";
    }

    @GetMapping("/cancel")
    public String cancel() {
        return "redirect:/tokenChargeForm?result=cancel";
    }
}
