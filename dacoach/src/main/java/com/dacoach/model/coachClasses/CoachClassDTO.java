package com.dacoach.model.coachClasses;

import java.util.Date;
import org.springframework.format.annotation.DateTimeFormat;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class CoachClassDTO {

	// ========== CLASS 테이블 기본 컬럼 (DB 실제 컬럼) ==========
	private Integer class_idx; // 클래스 인덱스 (PK)
	private Integer provider_idx; // 제공자 유저 인덱스 (FK)
	private String title; // 클래스 제목
	private String intro; // 클래스 소개
	private Integer minor_field_idx; // 클래스 분야 소분류 인덱스 (FK)
	private Integer minor_region_idx; // 클래스 장소 소분류 인덱스 (FK)
	private String region_detail; // 상세 주소
	private Integer price; // 가격 (원 단위)
	private String photo; // 클래스 소개 사진 경로
	private String video; // 클래스 소개 영상 경로

	@DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
	private Date start_date; // 시작 일시

	@DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
	private Date end_date; // 종료 일시

	private Date created_at; // 등록일
	private Integer max_user_cnt; // 수강 인원 제한

	// ========== 화면 표시용 추가 필드 (조인/계산 결과) ==========
	// ※ 주의: 아래 필드들은 DB 테이블 컬럼이 아니며, SELECT 쿼리 결과로만 채워집니다
	private String user_name; // 제공자 이름 (USERS 테이블 조인)
	private String provider_name; // 제공자 이름 (USERS 테이블 조인) - user_name과 동일
	private Double avg_rating; // 평균 별점 (REVIEW_CLASS 집계)
	private Integer review_cnt; // 리뷰 개수 (REVIEW_CLASS 집계)
	private Integer is_premium; // 프리미엄 여부 (MEMBERSHIP 조인 계산 결과: 1=프리미엄, 0=일반)
}