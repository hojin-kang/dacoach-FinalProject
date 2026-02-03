package com.dacoach.admin.company.model;

import com.dacoach.model.company.CompanyDTO;
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
}
