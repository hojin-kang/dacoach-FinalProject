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

import jakarta.servlet.http.HttpServletRequest;

@Service
public class KakaoService {
	//카카오 토큰 발급 메서드
	public String getAccessToken(String code, String uri) {
	    String accessToken = "";
	    String reqURL = "https://kauth.kakao.com/oauth/token";

	    try {
	    	//문자열로 된 주소를 자바가 통신할 수 있는 URL 객체로 변환
	        URL url = new URL(reqURL);
	        //URL 객체를 통해 통신 통로 생성
	        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
	        //카카오 api 명세서 요청에 따라 전송방식은 POST
	        conn.setRequestMethod("POST");
	        //이 연결을 통해 데이터를 서버로 전송할 것인지 결정
	        //true-전송, 수신/ false-수신만
	        conn.setDoOutput(true);
	        
	        // 헤더 설정, 카카오측으로 보내는 데이터의 형식 지정
	        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");
	        
	        // 전송할 파라미터 문자열 생성, redirect_uri는 카카오 개발자 사이트에 등록된 값(REST API키의 카카오 로그인 리다리렉트 URI와 동일해야 함
	        String params = "grant_type=authorization_code"
	                      + "&client_id=836bfad03ae2127905c6623948062759"
	                      + "&redirect_uri="+uri
	                      + "&code=" + code;

	        // 데이터를 byte 배열로 직접 전송 (인코딩 문제 방지)
	        byte[] postData = params.getBytes(StandardCharsets.UTF_8);
	        //괄호안에 작성 시 자동으로 close() 호출
	        try (OutputStream os = conn.getOutputStream()) {
	            os.write(postData);
	        }
	        //토큰 요청 응답 코드 확인
	        int responseCode = conn.getResponseCode();

	        InputStream is = (responseCode == 200) ? conn.getInputStream() : conn.getErrorStream();
	        BufferedReader br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
	        
	        StringBuilder result = new StringBuilder();
	        String line;
	        while ((line = br.readLine()) != null) { result.append(line); }

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
        //카카오 api 전용 주소
        String reqURL = "https://kapi.kakao.com/v2/user/me";

        try {
            URL url = new URL(reqURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            //요청 헤더에 액세스 토큰 포함
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

            JSONObject json = new JSONObject(result.toString());
            
            // ID 추출 (getLong 대신 get 후 toString이 안전)
            String id = json.get("id").toString();
            
            // 닉네임 추출 (properties가 없을 경우 대비)
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
