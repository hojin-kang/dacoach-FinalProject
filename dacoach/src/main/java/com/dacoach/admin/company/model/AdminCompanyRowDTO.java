package com.dacoach.admin.company.model;

import com.dacoach.model.admin.EmbeddedUserDTO;
import com.dacoach.model.classes.ClassDTO;
import com.dacoach.model.company.CompanyDTO;
import com.dacoach.model.review.ReviewClassDTO;
import com.dacoach.model.review.ReviewClassSummaryDTO;
import com.dacoach.model.users.UsersDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class AdminCompanyRowDTO {
	private int users_idx;
    private CompanyDTO company;   // COMPANY 테이블 컬럼들
    private UsersDTO users;       // USERS 테이블 컬럼들
    private String cert_status;   // CERT_STATUS (사업자등록증만)
    private EmbeddedUserDTO embedded; // 정지 정보
    private ClassDTO classes;    // 클래스 정보
    private ReviewClassSummaryDTO reviewSummary; // 리뷰 요약 정보
    private ReviewClassDTO review; // 리뷰 정보
}
