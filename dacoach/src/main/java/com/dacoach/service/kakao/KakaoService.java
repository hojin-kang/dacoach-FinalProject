package com.dacoach.service.kakao;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;
import org.springframework.stereotype.Service;

@Service
public class KakaoService {

	public String getAccessToken(String code) {
	    String accessToken = "";
	    String reqURL = "https://kauth.kakao.com/oauth/token";

	    try {
	        URL url = new URL(reqURL);
	        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
	        conn.setRequestMethod("POST");
	        conn.setDoOutput(true);
	        
	        // 헤더 설정 (띄어쓰기 주의)
	        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");

	        // 전송할 파라미터 문자열 생성
	        String params = "grant_type=authorization_code"
	                      + "&client_id=836bfad03ae2127905c6623948062759"
	                      + "&redirect_uri=http://localhost:9090/auth/kakao/callback"
	                      + "&code=" + code;

	        // 데이터를 byte 배열로 직접 전송 (인코딩 문제 방지)
	        byte[] postData = params.getBytes(StandardCharsets.UTF_8);
	        try (OutputStream os = conn.getOutputStream()) {
	            os.write(postData);
	        }

	        int responseCode = conn.getResponseCode();
	        System.out.println("토큰 요청 응답 코드 : " + responseCode);

	        InputStream is = (responseCode == 200) ? conn.getInputStream() : conn.getErrorStream();
	        BufferedReader br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
	        
	        StringBuilder result = new StringBuilder();
	        String line;
	        while ((line = br.readLine()) != null) { result.append(line); }
	        System.out.println("토큰 요청 최종 응답 : " + result.toString());

	        if (responseCode == 200) {
	            JSONObject json = new JSONObject(result.toString());
	            accessToken = json.getString("access_token");
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    return accessToken;
	}

    public Map<String, Object> getUserInfo(String accessToken) {
        Map<String, Object> userInfo = new HashMap<>();
        String reqURL = "https://kapi.kakao.com/v2/user/me";

        try {
            URL url = new URL(reqURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Authorization", "Bearer " + accessToken);

            int responseCode = conn.getResponseCode();
            System.out.println("디버깅 - 응답 코드 : " + responseCode);

            BufferedReader br;
            if (responseCode == 200) { // 정상 응답
                br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            } else { // 에러 응답
                br = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
            }

            String line = "";
            StringBuilder result = new StringBuilder();
            while ((line = br.readLine()) != null) {
                result.append(line);
            }
            System.out.println("디버깅 - 카카오 응답 데이터 : " + result.toString());

            JSONObject json = new JSONObject(result.toString());
            
            // 1. ID 추출 (getLong 대신 get 후 toString이 안전)
            String id = json.get("id").toString();
            
            // 2. 닉네임 추출 (properties가 없을 경우 대비)
            String nickname = "카카오사용자"; // 기본값
            if (json.has("properties")) {
                JSONObject properties = json.getJSONObject("properties");
                if (properties.has("nickname")) {
                    nickname = properties.getString("nickname");
                }
            }

            userInfo.put("id", id);
            userInfo.put("nickname", nickname);
            
            br.close();
        } catch (Exception e) {
            System.err.println("getUserInfo 중 예외 발생!");
            e.printStackTrace();
        }
        return userInfo;
    }
}
