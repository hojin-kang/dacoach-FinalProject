package com.dacoach.service.mail;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.InternetAddress;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.TemplateEngine;

import lombok.RequiredArgsConstructor;
import java.io.UnsupportedEncodingException;

import com.dacoach.mapper.coach.CoachMapper;

@Service
@RequiredArgsConstructor
public class MailService {
	@Autowired
	private CoachMapper coachMapper;

	private final JavaMailSender mailSender;
	private final TemplateEngine templateEngine;

	public void sendVerificationEmail(String toEmail, String authCode) {
		MimeMessage message = mailSender.createMimeMessage();

		try {
			MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
			helper.setTo(toEmail);
			helper.setSubject("[다코치] 인증번호");

			// Thymeleaf 템플릿 처리
			Context context = new Context();
			context.setVariable("authCode", authCode);
			String htmlContent = templateEngine.process("mail/auth", context);

			helper.setText(htmlContent, true);
			helper.setFrom(new InternetAddress("dacoach@dacoach.com", "다코치 팀", "UTF-8"));
			// 프로젝트의 src/main/resources/static/img/logo.png

			ClassPathResource logoImage = new ClassPathResource("static/img/logo.png");
			helper.addInline("logo", logoImage);

			mailSender.send(message);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public String getId(String email) {
		String id=null;
		try {
			id = coachMapper.getId(email);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return id;
	}
	public String getCompanyId(String email) {
		String id=null;
		try {
			id = coachMapper.getCompanyId(email);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return id;
	}
}