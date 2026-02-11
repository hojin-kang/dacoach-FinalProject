package com.dacoach.model.coachClasses;

import java.util.Date;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class ClassEnrollmentDTO {

	// ===== CLASS_ENROLLMENT 테이블 기본 필드 =====
	private Integer enroll_idx; // 수강 신청 인덱스 (PK)
	private Integer class_idx; // 클래스 인덱스 (FK)
	private Integer user_idx; // 수강생 유저 인덱스 (FK)
	private String status; // 수강 상태 (PENDING/CONFIRMED/CANCELLED/COMPLETED)
	private Date enrolled_at; // 수강 신청일
	private Date completed_at; // 수강 완료일 및 취소일

	// ===== 조인 필드 (화면 표시용) =====
	private String class_title; // 클래스 제목
	private String user_name; // 수강생 이름
	private String provider_name; // 제공자 이름
	private Integer price; // 클래스 가격
	private Date start_date; // 클래스 시작일
	private Date end_date; // 클래스 종료일
}