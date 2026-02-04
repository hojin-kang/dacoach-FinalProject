package com.dacoach.service.mail;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.InternetAddress;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import java.io.UnsupportedEncodingException;

@Service
@RequiredArgsConstructor
public class MailService {

    private final JavaMailSender mailSender;

    public void sendEmail(String toEmail, String title, String text) {
        MimeMessage message = mailSender.createMimeMessage();
        
        try {
            // true는 멀티파트 메시지(파일 첨부 등)를 지원한다는 의미입니다.
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(toEmail);
            helper.setSubject(title);
            helper.setText(text, false); // false는 HTML이 아닌 일반 텍스트 모드

            // 발신자 설정: (이메일 주소, 표시될 이름, 인코딩)
            helper.setFrom(new InternetAddress("dacoach@dacoach.com", "dacoach", "UTF-8"));

            mailSender.send(message);
            
        } catch (MessagingException | UnsupportedEncodingException e) {
            // 에러 핸들링 (로깅 등)
            e.printStackTrace();
        }
    }
}